package com.zs.common.core.utils;

import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.zs.common.core.model.domain.SysDictDataDTO;
import com.zs.common.redis.config.RedisUtil;
import jakarta.validation.constraints.NotNull;

import java.util.*;
import java.util.stream.Collectors;


/**
 * @author zsadmin
 */
public class DictRedisUtil {

    /** 缓存字典数据 */
    public static void set(String key, List<SysDictDataDTO> sysDictDataDTOList) {
        SpringUtil.getBean(RedisUtil.class).setObject(key, sysDictDataDTOList);
    }

    /** 获取字典数据 */
    @NotNull
    public static List<SysDictDataDTO> get(String key) {
        return Optional.ofNullable(SpringUtil.getBean(RedisUtil.class).get(key)).map(obj -> JSONUtil.toList(JSONUtil.toJsonStr(obj),
                SysDictDataDTO.class)).orElse(Collections.emptyList());
    }

    /** 批量获取字典数据 */
    @NotNull
    public static List<SysDictDataDTO> getMulti(Collection<String> keys) {
        List<Object> objectList = SpringUtil.getBean(RedisUtil.class).getMulti(keys);
        return Objects.requireNonNull(objectList).stream()
                .map(obj -> JSONUtil.toList(JSONUtil.toJsonStr(obj), SysDictDataDTO.class))
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

}
