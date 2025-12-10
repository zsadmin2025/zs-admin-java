package com.zs.sys.dict.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.common.core.constant.RedisConstants;
import com.zs.common.core.enums.StatusEnum;
import com.zs.common.core.page.PageInfo;
import com.zs.common.core.page.PageResult;
import com.zs.common.core.utils.TreeUtil;
import com.zs.common.redis.config.RedisUtil;
import com.zs.sys.dict.domain.entity.SysDictDataEntity;
import com.zs.sys.dict.domain.params.SysDictDataAddParams;
import com.zs.sys.dict.domain.params.SysDictDataPageQueryParams;
import com.zs.sys.dict.domain.params.SysDictDataSelectQueryParams;
import com.zs.sys.dict.domain.params.SysDictDataUpdateParams;
import com.zs.sys.dict.domain.vo.SysDictDataVO;
import com.zs.sys.dict.mapper.SysDictDataMapper;
import com.zs.sys.dict.service.ISysDictDataService;
import jakarta.annotation.Nullable;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author zsadmin
 */
@Service
public class SysDictDataServiceImpl extends ServiceImpl<SysDictDataMapper, SysDictDataEntity> implements ISysDictDataService {

    @Resource
    private RedisUtil redisUtil;


    @NotNull
    @Override
    public PageResult<SysDictDataVO> page(@NotNull SysDictDataPageQueryParams sysDictDataPageQueryParams) {
        Page<SysDictDataEntity> page = new PageInfo<>(sysDictDataPageQueryParams);

        IPage<SysDictDataEntity> iPage = baseMapper.selectPage(page, getQueryWrapper(sysDictDataPageQueryParams));
        List<SysDictDataVO> list = BeanUtil.copyToList(iPage.getRecords(), SysDictDataVO.class);

        List<SysDictDataVO> treeList = TreeUtil.build(list, 0L);
        // 清理空 children
        TreeUtil.cleanEmptyChildren(treeList);
        return new PageResult<>(treeList, iPage.getTotal(), SysDictDataVO.class);
    }

    @Override
    public  Map<String, List<SysDictDataVO>> getList() {
        List<SysDictDataEntity> entityList = baseMapper.selectList(new LambdaQueryWrapper<SysDictDataEntity>().eq(SysDictDataEntity::getStatus, StatusEnum.NORMAL.getValue()));

        // 转换为 VO 列表
        List<SysDictDataVO> voList = entityList.stream().map(e -> BeanUtil.copyProperties(e, SysDictDataVO.class)).toList();

        List<SysDictDataVO> tree = TreeUtil.build(BeanUtil.copyToList(voList, SysDictDataVO.class), 0L);
        // 清理空 children
        TreeUtil.cleanEmptyChildren(tree);

        // 按 dictType 分组 构建最终结果 Map<String, List<SysDictDataMapVO>>
        return tree.stream().collect(Collectors.groupingBy(SysDictDataVO::getDictType));

    }

    @Nullable
    @Override
    public List<SysDictDataVO> dictTypeList(@NotNull SysDictDataSelectQueryParams sysDictDataSelectQueryParams) {
        // 从 redis缓存中获取字典数据
        Object  object =  redisUtil.get(RedisConstants.SYS_DICT_KEY + sysDictDataSelectQueryParams.getDictType());

        List<SysDictDataVO> sysDictDataDTOList = JSONUtil.toList(JSONUtil.toJsonStr(object), SysDictDataVO.class);
        List<SysDictDataVO> tree = TreeUtil.build(BeanUtil.copyToList(sysDictDataDTOList, SysDictDataVO.class), 0L);
        // 清理空 children
        TreeUtil.cleanEmptyChildren(tree);
        return tree;
    }


    @NotNull
    private QueryWrapper<SysDictDataEntity> getQueryWrapper(@NotNull SysDictDataPageQueryParams sysDictDataQueryParams) {
        QueryWrapper<SysDictDataEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(Objects.nonNull(sysDictDataQueryParams.getSysDictTypeId()), "sys_dict_type_id", sysDictDataQueryParams.getSysDictTypeId());
        wrapper.like(Strings.isNotEmpty(sysDictDataQueryParams.getDictType()), "dict_type", sysDictDataQueryParams.getDictType());
        wrapper.like(Strings.isNotEmpty(sysDictDataQueryParams.getDictLabel()), "dict_label", sysDictDataQueryParams.getDictLabel());
        wrapper.like(Strings.isNotEmpty(sysDictDataQueryParams.getDictValue()), "dict_value", sysDictDataQueryParams.getDictValue());
        return wrapper;
    }

    @Override
    public SysDictDataVO getById(Long id) {
        return BeanUtil.copyProperties(baseMapper.selectById(id), SysDictDataVO.class);
    }

    @Override
    public void save(SysDictDataAddParams sysDictDataAddParams) {
        SysDictDataEntity sysDictDataEntity = BeanUtil.copyProperties(sysDictDataAddParams, SysDictDataEntity.class);
        int row = baseMapper.insert(sysDictDataEntity);
        if (row > 0) {
            saveDictDataRedis(sysDictDataEntity.getDictType());
        }
    }

    @Override
    public void update(SysDictDataUpdateParams sysDictDataUpdateParams) {
        SysDictDataEntity sysDictDataEntity = BeanUtil.copyProperties(sysDictDataUpdateParams, SysDictDataEntity.class);
        int row = baseMapper.updateById(sysDictDataEntity);
        if (row > 0) {
            saveDictDataRedis(sysDictDataEntity.getDictType());
        }
    }

    @Override
    public void deleteById(Long id) {
        baseMapper.deleteById(id);
        List<SysDictDataEntity> sysDictDataEntityList = baseMapper.selectList(new QueryWrapper<SysDictDataEntity>().eq("status", 1));
        if (!sysDictDataEntityList.isEmpty()) {
            redisUtil.setObject(RedisConstants.SYS_DICT_KEY + sysDictDataEntityList.get(0).getDictType(), sysDictDataEntityList);
        }
    }

    @Override
    public void batchDelById(Long[] sysDictDataIds) {
        baseMapper.deleteByIds(Arrays.asList(sysDictDataIds));
        saveCache();
    }

    @PostConstruct
    public void saveCache() {
        List<SysDictDataEntity> sysDictDataEntityList = baseMapper.selectList(new QueryWrapper<SysDictDataEntity>().eq("status", 1));
        Map<String, List<SysDictDataEntity>> sysDictDataMap = sysDictDataEntityList.stream().collect(Collectors.groupingBy(SysDictDataEntity::getDictType));
        for (Map.Entry<String, List<SysDictDataEntity>> entry : sysDictDataMap.entrySet()) {
            // 保存字典数据到redis缓存
            redisUtil.setObject(RedisConstants.SYS_DICT_KEY + entry.getKey(), entry.getValue());
        }
    }


    private void saveDictDataRedis(String dictType) {
        List<SysDictDataEntity> sysDictDataEntityList = baseMapper.selectList(new LambdaQueryWrapper<SysDictDataEntity>().eq(SysDictDataEntity::getDictType, dictType).eq(SysDictDataEntity::getStatus, 1));
        redisUtil.setObject(RedisConstants.SYS_DICT_KEY + dictType, sysDictDataEntityList);
    }
}
