package com.zs.business.warehouse.warehouse.info.service.impl;

import com.zs.business.warehouse.warehouse.info.domain.entity.BusinessWarehouseInfoEntity;
import com.zs.business.warehouse.warehouse.info.mapper.BusinessWarehouseInfoMapper;
import com.zs.business.warehouse.warehouse.info.service.BusinessWarehouseInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.sys.api.role.RemoteUserService;
import org.springframework.stereotype.Service;
import com.zs.common.core.page.PageInfo;
import com.zs.common.core.page.PageResult;
import com.zs.business.warehouse.warehouse.info.domain.vo.BusinessWarehouseInfoVO;
import com.zs.business.warehouse.warehouse.info.domain.params.BusinessWarehouseInfoPageQueryParams;
import com.zs.business.warehouse.warehouse.info.domain.params.BusinessWarehouseInfoSelectQueryParams;
import com.zs.business.warehouse.warehouse.info.domain.params.BusinessWarehouseInfoAddParams;
import com.zs.business.warehouse.warehouse.info.domain.params.BusinessWarehouseInfoUpdateParams;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import cn.hutool.core.util.ObjectUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 库房表 服务实现类
 * </p>
 *
 * @author zs
 * {@code @date} 2026-08-04 11:38:39
 */
@Service
public class BusinessWarehouseInfoServiceImpl extends ServiceImpl<BusinessWarehouseInfoMapper, BusinessWarehouseInfoEntity> implements BusinessWarehouseInfoService {

    @Resource
    private RemoteUserService remoteUserService;

        @Override
        public PageResult<BusinessWarehouseInfoVO> page(@NotNull BusinessWarehouseInfoPageQueryParams businessWarehouseInfoPageQueryParams) {
            Page<BusinessWarehouseInfoEntity> page = new PageInfo<>(businessWarehouseInfoPageQueryParams);
            LambdaQueryWrapper<BusinessWarehouseInfoEntity> wrapper = new LambdaQueryWrapper<>();
            wrapper.like(ObjectUtil.isNotEmpty(businessWarehouseInfoPageQueryParams.getWarehouseCode()), BusinessWarehouseInfoEntity::getWarehouseCode, businessWarehouseInfoPageQueryParams.getWarehouseCode());
            wrapper.like(ObjectUtil.isNotEmpty(businessWarehouseInfoPageQueryParams.getWarehouseName()), BusinessWarehouseInfoEntity::getWarehouseName, businessWarehouseInfoPageQueryParams.getWarehouseName());
            wrapper.eq(ObjectUtil.isNotEmpty(businessWarehouseInfoPageQueryParams.getWarehouseType()), BusinessWarehouseInfoEntity::getWarehouseType, businessWarehouseInfoPageQueryParams.getWarehouseType());
            wrapper.eq(ObjectUtil.isNotEmpty(businessWarehouseInfoPageQueryParams.getStatus()), BusinessWarehouseInfoEntity::getStatus, businessWarehouseInfoPageQueryParams.getStatus());
            IPage<BusinessWarehouseInfoEntity> iPage = baseMapper.selectPage(page, wrapper);
            List<BusinessWarehouseInfoVO> list = BeanUtil.copyToList(iPage.getRecords(), BusinessWarehouseInfoVO.class);

            // 批量查询管理员姓名
            List<Long> managerUserIds = iPage.getRecords().stream().map(BusinessWarehouseInfoEntity::getManagerUserId).filter(ObjectUtil::isNotNull).distinct().toList();
            if (!managerUserIds.isEmpty()) {
                Map<Long, String> userNameMap = remoteUserService.getUserNameMap(managerUserIds);
                for (int i = 0; i < list.size(); i++) {
                    BusinessWarehouseInfoEntity entity = iPage.getRecords().get(i);
                    if (entity.getManagerUserId() != null) {
                        list.get(i).setManagerUserName(userNameMap.get(entity.getManagerUserId()));
                    }
                }
            }

            return new PageResult<>(list, page.getTotal(), BusinessWarehouseInfoVO.class);
        }

        @Override
        public List<BusinessWarehouseInfoVO> getList(@NotNull BusinessWarehouseInfoSelectQueryParams businessWarehouseInfoSelectQueryParams) {
            LambdaQueryWrapper<BusinessWarehouseInfoEntity> wrapper = new LambdaQueryWrapper<>();
            wrapper.like(ObjectUtil.isNotEmpty(businessWarehouseInfoSelectQueryParams.getWarehouseCode()), BusinessWarehouseInfoEntity::getWarehouseCode, businessWarehouseInfoSelectQueryParams.getWarehouseCode());
            wrapper.like(ObjectUtil.isNotEmpty(businessWarehouseInfoSelectQueryParams.getWarehouseName()), BusinessWarehouseInfoEntity::getWarehouseName, businessWarehouseInfoSelectQueryParams.getWarehouseName());
            wrapper.eq(ObjectUtil.isNotEmpty(businessWarehouseInfoSelectQueryParams.getWarehouseType()), BusinessWarehouseInfoEntity::getWarehouseType, businessWarehouseInfoSelectQueryParams.getWarehouseType());
            wrapper.eq(ObjectUtil.isNotEmpty(businessWarehouseInfoSelectQueryParams.getStatus()), BusinessWarehouseInfoEntity::getStatus, businessWarehouseInfoSelectQueryParams.getStatus());
            List<BusinessWarehouseInfoEntity> entityList = baseMapper.selectList(wrapper);
            List<BusinessWarehouseInfoVO> list = BeanUtil.copyToList(entityList, BusinessWarehouseInfoVO.class);

            // 批量查询管理员姓名
            List<Long> managerUserIds = entityList.stream().map(BusinessWarehouseInfoEntity::getManagerUserId).filter(ObjectUtil::isNotNull).distinct().toList();
            if (!managerUserIds.isEmpty()) {
                Map<Long, String> userNameMap = remoteUserService.getUserNameMap(managerUserIds);
                for (int i = 0; i < list.size(); i++) {
                    BusinessWarehouseInfoEntity entity = entityList.get(i);
                    if (entity.getManagerUserId() != null) {
                        list.get(i).setManagerUserName(userNameMap.get(entity.getManagerUserId()));
                    }
                }
            }

            return list;
        }

        @Override
        public void save(@NotNull BusinessWarehouseInfoAddParams businessWarehouseInfoAddParams) {
            BusinessWarehouseInfoEntity businessWarehouseInfoEntity = BeanUtil.copyProperties(businessWarehouseInfoAddParams, BusinessWarehouseInfoEntity.class);
            baseMapper.insert(businessWarehouseInfoEntity);
        }

        @Override
        public void update(@NotNull BusinessWarehouseInfoUpdateParams businessWarehouseInfoUpdateParams) {
            BusinessWarehouseInfoEntity businessWarehouseInfoEntity = BeanUtil.copyProperties(businessWarehouseInfoUpdateParams, BusinessWarehouseInfoEntity.class);
            baseMapper.updateById(businessWarehouseInfoEntity);
        }

        @Override
        public BusinessWarehouseInfoVO getById(Long id) {
            return BeanUtil.copyProperties(baseMapper.selectById(id), BusinessWarehouseInfoVO.class);
        }

        @Override
        public void deleteById(Long id) {
            baseMapper.deleteById(id);
        }

        @Override
        public void batchDelById(@NotNull Long[] ids) {
            baseMapper.deleteByIds(Arrays.asList(ids));
        }
}
