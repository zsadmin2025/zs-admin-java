package com.zs.common.mp.base;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.apache.ibatis.session.ResultHandler;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface DataPermissionMapper<T> extends BaseMapper<T> {

    T selectById(Serializable id);

    @Override
    List<T> selectBatchIds(@Param("coll") Collection<? extends Serializable> idList);

    void selectBatchIds(@Param("coll") Collection<? extends Serializable> idList, ResultHandler<T> resultHandler);

    default List<T> selectByMap(Map<String, Object> columnMap) {
        return this.selectList(Wrappers.<T>query().allEq(columnMap));
    }

    default void selectByMap(Map<String, Object> columnMap, ResultHandler<T> resultHandler) {
        this.selectList( Wrappers.<T>query().allEq(columnMap), resultHandler);
    }

    default T selectOne(@Param("ew") Wrapper<T> queryWrapper) {
        return (T) this.selectOne(queryWrapper, true);
    }

    default T selectOne(@Param("ew") Wrapper<T> queryWrapper, boolean throwEx) {
        List<T> list = this.selectList(queryWrapper);
        int size = list.size();
        if (size == 1) {
            return (T) list.get(0);
        } else if (size > 1) {
            if (throwEx) {
                throw new TooManyResultsException("Expected one result (or null) to be returned by selectOne(), but found: " + size);
            } else {
                return (T) list.get(0);
            }
        } else {
            return null;
        }
    }

    Long selectCount(@Param("ew") Wrapper<T> queryWrapper);

    List<T> selectList(@Param("ew") Wrapper<T> queryWrapper);

    void selectList(@Param("ew") Wrapper<T> queryWrapper, ResultHandler<T> resultHandler);

    List<T> selectList(IPage<T> page, @Param("ew") Wrapper<T> queryWrapper);

    void selectList(IPage<T> page, @Param("ew") Wrapper<T> queryWrapper, ResultHandler<T> resultHandler);

    List<Map<String, Object>> selectMaps(@Param("ew") Wrapper<T> queryWrapper);

    void selectMaps(@Param("ew") Wrapper<T> queryWrapper, ResultHandler<Map<String, Object>> resultHandler);

    List<Map<String, Object>> selectMaps(IPage<? extends Map<String, Object>> page, @Param("ew") Wrapper<T> queryWrapper);

    void selectMaps(IPage<? extends Map<String, Object>> page, @Param("ew") Wrapper<T> queryWrapper, ResultHandler<Map<String, Object>> resultHandler);

    <E> List<E> selectObjs(@Param("ew") Wrapper<T> queryWrapper);

    <E> void selectObjs(@Param("ew") Wrapper<T> queryWrapper, ResultHandler<E> resultHandler);

    default <P extends IPage<T>> P selectPage(P page, @Param("ew") Wrapper<T> queryWrapper) {
        page.setRecords(this.selectList(page, queryWrapper));
        return page;
    }

    default <P extends IPage<Map<String, Object>>> P selectMapsPage(P page, @Param("ew") Wrapper<T> queryWrapper) {
        page.setRecords(this.selectMaps(page, queryWrapper));
        return page;
    }

}
