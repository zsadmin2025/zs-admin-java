package com.zs.common.aop.aspect;

import cn.hutool.json.JSONUtil;
import com.zs.common.core.annotation.DictBind;
import com.zs.common.core.constant.RedisConstants;
import com.zs.common.core.model.domain.SysDictDataDTO;
import com.zs.common.redis.config.RedisUtil;
import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 字典自动填充 —— 注解 + AOP + 反射 + Redis 直读，单文件搞定
 * <p>
 * 全局拦截所有 Controller 返回值，检测 {@code @DictBind} 字段，
 * 批量聚合查询 Redis 字典缓存后回填 label。
 * </p>
 *
 * <h3>用法</h3>
 * <pre>{@code
 * // VO 中只需一行注解：
 * private Long partnerType;
 *
 * @DictBind(dictCode = "partner_type", sourceField = "partnerType", defaultValue = "未知")
 * private String partnerTypeLabel;
 *
 * // Controller 零改动，自动生效
 * }</pre>
 *
 * @author zsadmin
 */
@Slf4j
@Aspect
@Component
public class DictFillAspect {

    @Resource
    private RedisUtil redisUtil;

    /** Class 字段元数据缓存，首次解析后复用 */
    private static final Map<Class<?>, List<DictMeta>> META_CACHE = new ConcurrentHashMap<>(128);
    /** 已确认无 @DictBind 的类，跳过不扫 */
    private static final Set<Class<?>> NO_DICT = ConcurrentHashMap.newKeySet(128);

    // ════════════════ AOP ════════════════

    @AfterReturning(
            value = "@within(org.springframework.web.bind.annotation.RestController) || @within(org.springframework.stereotype.Controller)",
            returning = "result"
    )
    public void afterReturning(JoinPoint joinPoint, Object result) {
        if (result == null) return;
        try {
            fill(result);
        } catch (Exception e) {
            log.error("字典填充失败 {}: {}", joinPoint.getSignature().toShortString(), e.getMessage(), e);
        }
    }

    // ════════════════ 入口分发 ════════════════

    @SuppressWarnings("unchecked")
    private void fill(Object data) {
        if (data instanceof List) {
            fillList((List<?>) data);
            return;
        }
        // Result 包装
        if ("com.zs.common.core.core.Result".equals(data.getClass().getName())) {
            fill(unwrap(data, "data"));
            return;
        }
        // PageResult 分页
        if ("com.zs.common.core.page.PageResult".equals(data.getClass().getName())) {
            fillList((List<?>) unwrap(data, "list"));
            return;
        }
        // 普通对象
        fillList(Collections.singletonList(data));
    }

    private Object unwrap(Object obj, String fieldName) {
        try {
            Field f = obj.getClass().getDeclaredField(fieldName);
            f.setAccessible(true);
            return f.get(obj);
        } catch (Exception e) {
            return null;
        }
    }

    // ════════════════ 核心：收 → 查 → 填 ════════════════

    private void fillList(List<?> list) {
        Map<Class<?>, List<Object>> grouped = list.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(Object::getClass));
        if (grouped.isEmpty()) return;

        // ① 收：遍历收集所有待翻译值 → {dictCode: [value1, value2]}
        Map<String, Set<String>> toQuery = new LinkedHashMap<>();
        for (var e : grouped.entrySet()) {
            for (DictMeta m : metas(e.getKey())) {
                for (Object obj : e.getValue()) {
                    Object v = getVal(m.srcField, obj);
                    if (v != null) toQuery.computeIfAbsent(m.dictCode, k -> new LinkedHashSet<>()).add(v.toString());
                }
            }
        }
        if (toQuery.isEmpty()) return;

        // ② 查：每种 dictCode 一次 Redis 读取 → {dictCode: {value: label}}
        Map<String, Map<String, String>> labels = new HashMap<>();
        for (var e : toQuery.entrySet()) {
            labels.put(e.getKey(), loadDict(e.getKey()));
        }

        // ③ 填：回写 label
        for (var e : grouped.entrySet()) {
            for (DictMeta m : metas(e.getKey())) {
                Map<String, String> map = labels.get(m.dictCode);
                for (Object obj : e.getValue()) {
                    Object v = getVal(m.srcField, obj);
                    String label = (v != null && map != null) ? map.get(v.toString()) : null;
                    setVal(m.lblField, obj, label != null ? label : m.defVal);
                }
            }
        }
    }

    // ════════════════ 元数据缓存 ════════════════

    private List<DictMeta> metas(Class<?> clazz) {
        if (NO_DICT.contains(clazz)) return Collections.emptyList();
        return META_CACHE.computeIfAbsent(clazz, k -> {
            List<DictMeta> list = new ArrayList<>();
            for (Field f : k.getDeclaredFields()) {
                DictBind ann = f.getAnnotation(DictBind.class);
                if (ann == null) continue;
                Field src;
                try { src = k.getDeclaredField(ann.sourceField()); }
                catch (NoSuchFieldException ex) { continue; }
                src.setAccessible(true);
                f.setAccessible(true);
                list.add(new DictMeta(ann.dictCode(), src, f, ann.defaultValue()));
            }
            if (list.isEmpty()) NO_DICT.add(k);
            return list;
        });
    }

    // ════════════════ Redis 读取 ════════════════

    private Map<String, String> loadDict(String dictCode) {
        Object cached = redisUtil.get(RedisConstants.SYS_DICT_KEY + dictCode);
        if (cached == null) return Collections.emptyMap();
        List<SysDictDataDTO> list = JSONUtil.toList(JSONUtil.toJsonStr(cached), SysDictDataDTO.class);
        if (list == null || list.isEmpty()) return Collections.emptyMap();
        return list.stream().collect(Collectors.toMap(
                SysDictDataDTO::getDictValue, SysDictDataDTO::getDictLabel, (a, b) -> a));
    }

    // ════════════════ 反射 ════════════════

    private static Object getVal(Field f, Object obj) {
        try { return f.get(obj); } catch (Exception e) { return null; }
    }

    private static void setVal(Field f, Object obj, Object val) {
        try { f.set(obj, val); } catch (Exception ignored) {}
    }

    // ════════════════ 内部类 ════════════════

    @AllArgsConstructor
    private static class DictMeta {
        String dictCode;
        Field srcField;
        Field lblField;
        String defVal;
    }
}
