package com.zs.common.redis.config;

import jakarta.annotation.Nullable;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author zsadmin
 */
@Component
public class RedisUtil {
    @Resource
    public RedisTemplate<String, Object> redisTemplate;


    /**
     * @param key   key
     * @param value value
     * @param time  过期时间
     */
    public void setObject(@NotNull String key, @NotNull Object value, long time, @NotNull TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, time, timeUnit);
    }

    /**
     * String类型的set,无过期时间
     *
     * @param key   key
     * @param value value
     */
    public void setObject(@NotNull String key, @NotNull Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 批量设置key和value
     *
     * @param map key和value的集合
     */
    public void setMultiObject(@NotNull Map<String, Object> map) {
        redisTemplate.opsForValue().multiSet(map);
    }

    /**
     * 如果key不存在，则设置
     *
     * @param key   key
     * @param value value
     * @return 返回是否成功
     */
    @Nullable
    public Boolean setKey(@NotNull String key, @NotNull Object value) {
        return redisTemplate.opsForValue().setIfAbsent(key, value);
    }

    /**
     * 如果key不存在，则设置，并设置过期时间
     *
     * @param key   key
     * @param value value
     * @param time  过期时间
     */
    public void setKey(String key, String value, long time) {
        redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
    }

    /**
     * 批量插入key，如果key不存在的话
     *
     * @param map key和value的集合
     * @return 是否成功
     */
    @Nullable
    public Boolean setMultiKey(@NotNull Map<String, Object> map) {
        return redisTemplate.opsForValue().multiSetIfAbsent(map);
    }

    /**
     * String类型的get
     *
     * @param key key
     * @return 返回value对应的对象
     */
    @Nullable
    public Object get(@NotNull String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Nullable
    public List<Object> getMulti(@NotNull Collection<String> keys) {
        return redisTemplate.opsForValue().multiGet(keys);
    }

    /**
     * 删除对应key
     *
     * @param key key
     * @return 返回是否删除成功
     */
    @Nullable
    public Boolean del(@NotNull String key) {
        return redisTemplate.delete(key);
    }

    /**
     * 批量删除key
     *
     * @param keys key的集合
     * @return 返回删除成功的个数
     */
    @Nullable
    public Long del(@NotNull List<String> keys) {
        return redisTemplate.delete(keys);
    }

    /**
     * 获取List的所有元素
     *
     * @param key key
     * @return List中的所有元素
     */
    @Nullable
    public List<Object> listRange(@NotNull String key) {
        return redisTemplate.opsForList().range(key, 0, -1);
    }


    /**
     * 给某个key设置过期时间
     *
     * @param key  key
     * @param time 过期时间
     * @return 返回是否设置成功
     */
    @Nullable
    public Boolean expire(@NotNull String key, long time) {
        return redisTemplate.expire(key, time, TimeUnit.SECONDS);
    }

    /**
     * 返回某个key的过期时间
     *
     * @param key key
     * @return 返回key剩余的过期时间
     */
    @Nullable
    public Long getExpire(@NotNull String key) {
        return redisTemplate.getExpire(key);
    }

    /**
     * 返回是否存在该key
     *
     * @param key key
     * @return 是否存在该key
     */
    @Nullable
    public Boolean exists(@NotNull String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 给key的值加上delta值
     *
     * @param key   key
     * @param delta 参数
     * @return 返回key+delta的值
     */
    @Nullable
    public Long increment(@NotNull String key, long delta) {
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 给key的值减去delta
     *
     * @param key   key
     * @param delta 参数
     * @return 返回key - delta的值
     */
    @Nullable
    public Long decrement(@NotNull String key, long delta) {
        return redisTemplate.opsForValue().decrement(key, delta);
    }


    /**
     * set hash类型
     *
     * @param key     key
     * @param hashKey hashKey
     * @param value   value
     */
    public void hashKey(@NotNull String key, @NotNull String hashKey, @NotNull Object value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }


    /**
     * set hash类型,并设置过期时间
     *
     * @param key     key
     * @param hashKey hashKey
     * @param value   value
     * @param time    过期时间
     * @return 返回是否成功
     */
    public Boolean setHash(@NotNull String key, @NotNull String hashKey, @NotNull Object value, long time) {
        hashKey(key, hashKey, value);
        return expire(key, time);
    }

    /**
     * 批量设置hash
     *
     * @param key  key
     * @param map  hashKey和value的集合
     * @param time 过期时间
     * @return 是否成功
     */
    public Boolean setHash(@NotNull String key, @NotNull Map<String, Object> map, long time) {
        redisTemplate.opsForHash().putAll(key, map);
        return expire(key, time);
    }

    /**
     * set hash类型
     *
     * @param key     key
     * @param hashKey hashKey
     * @param value   value
     */
    public void setHash(@NotNull String key, @NotNull String hashKey, @NotNull Object value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    /**
     * 获取hash类型的值
     *
     * @param key     key
     * @param hashKey hashKey
     * @return 返回对应的value
     */
    @Nullable
    public Object getHash(@NotNull String key, @NotNull String hashKey) {
        return redisTemplate.opsForHash().get(key, hashKey);
    }

    /**
     * 获取key下所有的hash值以及hashKey
     *
     * @param key key
     * @return 返回数据
     */
    @NotNull
    public Map<Object, Object> entries(@NotNull String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 批量删除
     *
     * @param key     key
     * @param hashKey hashKey数组集合
     */
    public void deleteKeys(@NotNull String key, Object... hashKey) {
        redisTemplate.opsForHash().delete(key, hashKey);
    }

    /**
     * 判断是否存在hashKey
     *
     * @param key     key
     * @param hashKey hashKey
     * @return 是否存在
     */
    @NotNull
    public Boolean hasKey(@NotNull String key, @NotNull String hashKey) {
        return redisTemplate.opsForHash().hasKey(key, hashKey);
    }

    /**
     * 添加list类型的数据，左侧添加
     *
     * @param key   key
     * @param value value
     */
    public void addLeftList(@NotNull String key, @NotNull Object value) {
        redisTemplate.opsForList().leftPush(key, value);
    }

    /**
     * 添加list类型数据，右侧添加
     *
     * @param key   key
     * @param value value
     */
    public void addRightList(@NotNull String key, @NotNull Object value) {
        redisTemplate.opsForList().rightPush(key, value);
    }

    /**
     * 添加set类型数据
     *
     * @param key   key
     * @param value value
     */
    public void addSet(@NotNull String key, @NotNull Object value) {
        redisTemplate.opsForSet().add(key, value);
    }


    /**
     * 删除set类型数据
     *
     * @param key   key
     * @param value value
     */
    public void deleteSet(@NotNull String key, Object value) {
        redisTemplate.opsForSet().remove(key, value);
    }



    /**
     * 删除list类型数据
     *
     * @param key   key
     * @param value value
     */
    public void deleteList(@NotNull String key, Object value) {
        redisTemplate.opsForList().remove(key, 0, value);
    }

    /**
     * 删除list中指定下标的数据
     */
    public void deleteListByValue(@NotNull String key, Object value) {
        redisTemplate.opsForList().remove(key, 0, value); // 删除所有匹配项
    }


    // ==================== ZSet 操作 ====================

    /**
     * 添加zset类型数据
     *
     * @param key   key
     * @param value value
     * @param score 分数
     */
    public Boolean addZSet(@NotNull String key, @NotNull Object value, double score, long expireSeconds) {
        Boolean result = redisTemplate.opsForZSet().add(key, value, score);
        if (result != null && result && expireSeconds > 0) {
            redisTemplate.expire(key, expireSeconds, TimeUnit.SECONDS);
        }
        return result;
    }


    /**
     * 批量添加zset类型数据
     *
     * @param key    key
     * @param values value集合
     * @param score  分数
     */
    public void addZSet(@NotNull String key, @NotNull Set<Object> values, double score) {
        redisTemplate.opsForZSet().add(key, values, score);
    }

    /**
     * 获取zset类型的数据
     *
     * @param key key
     * @return 获取数据
     */
    public Set<Object> getZSet(@NotNull String key) {
        return redisTemplate.opsForZSet().range(key, 0, -1);
    }



    /**
     * 删除zset类型数据
     *
     * @param key   key
     * @param value value
     */
    public void deleteZSet(@NotNull String key, Object value) {
        redisTemplate.opsForZSet().remove(key, value);
    }


    /**
     * 获取 ZSet 中指定排名范围的成员（按 score 从高到低，倒序）
     */
    public Set<Object> zRevRange(String key, long start, long end) {
        return redisTemplate.opsForZSet().reverseRange(key, start, end);
    }
}
