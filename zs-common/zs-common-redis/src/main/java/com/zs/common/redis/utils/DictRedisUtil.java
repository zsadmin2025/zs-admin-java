//package com.zs.common.redis.utils;
//
//import cn.hutool.extra.spring.SpringUtil;
//import cn.hutool.json.JSONUtil;
//import com.zs.common.core.model.domain.SysDictDataDTO;
//import com.zs.common.redis.config.RedisUtil;
//import jakarta.validation.constraints.NotNull;
//
//import java.util.Collection;
//import java.util.Collections;
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//
///**
// * @author zsadmin
// */
//public class DictRedisUtil {
//
//    public static void set(String key, List<SysDictDataDTO> sysDictDataDTOList) {
//        SpringUtil.getBean(RedisUtil.class).setObject(key, sysDictDataDTOList);
//    }
//
//    @NotNull
//    public static List<SysDictDataDTO> get(String key) {
//        return Optional.ofNullable(SpringUtil.getBean(RedisUtil.class).get(key)).map(obj -> JSONUtil.toList(JSONUtil.toJsonStr(obj),
//                SysDictDataDTO.class)).orElse(Collections.emptyList());
//    }
//
//    @NotNull
//    public static List<SysDictDataDTO> getMulti(Collection<String> keys) {
//        List<Object> objectList = SpringUtil.getBean(RedisUtil.class).getMulti(keys);
//        return objectList.stream()
//                .map(obj -> JSONUtil.toList(JSONUtil.toJsonStr(obj), SysDictDataDTO.class))
//                .flatMap(List::stream)
//                .collect(Collectors.toList());
//    }
//
//}
