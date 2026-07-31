package com.zs.lawyer.customer.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.common.core.page.PageInfo;
import com.zs.common.core.page.PageResult;
import com.zs.lawyer.customer.domain.entity.CustomerEntity;
import com.zs.lawyer.customer.domain.params.CustomerAddParams;
import com.zs.lawyer.customer.domain.params.CustomerPageQueryParams;
import com.zs.lawyer.customer.domain.params.CustomerSelectQueryParams;
import com.zs.lawyer.customer.domain.params.CustomerUpdateParams;
import com.zs.lawyer.customer.domain.vo.CustomerVO;
import com.zs.lawyer.customer.mapper.CustomerMapper;
import com.zs.lawyer.customer.service.CustomerService;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 客户表 服务实现类
 * </p>
 *
 * @author zs
 * @since 2025-05-29 21:55:59
 */
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, CustomerEntity> implements CustomerService {

    @Override
    public PageResult<CustomerVO> page(@NotNull CustomerPageQueryParams customerPageQueryParams) {

        Page<CustomerEntity> page = new PageInfo<>(customerPageQueryParams);


        LambdaQueryWrapper<CustomerEntity> lambda = new LambdaQueryWrapper<CustomerEntity>();
        lambda.eq(StringUtils.isNotBlank(customerPageQueryParams.getCustomerCategory()), CustomerEntity::getCustomerCategory, customerPageQueryParams.getCustomerCategory());
        lambda.eq(StringUtils.isNotBlank(customerPageQueryParams.getCustomerNature()), CustomerEntity::getCustomerNature, customerPageQueryParams.getCustomerNature());
        lambda.eq(StringUtils.isNotBlank(customerPageQueryParams.getCustomerGrade()), CustomerEntity::getCustomerGrade, customerPageQueryParams.getCustomerGrade());
        lambda.in(!CollectionUtils.isEmpty(customerPageQueryParams.getMaintainingPeople()), CustomerEntity::getMaintainingPeople, customerPageQueryParams.getMaintainingPeople());
        lambda.in(!CollectionUtils.isEmpty(customerPageQueryParams.getSharer()), CustomerEntity::getSharer, customerPageQueryParams.getSharer());
        lambda.eq(Objects.nonNull(customerPageQueryParams.getInputPerson()), CustomerEntity::getInputPerson, customerPageQueryParams.getInputPerson());
        lambda.like(StringUtils.isNotBlank(customerPageQueryParams.getCustomerName()), CustomerEntity::getCustomerName, customerPageQueryParams.getCustomerName());
        lambda.eq(Objects.nonNull(customerPageQueryParams.getStatus()), CustomerEntity::getStatus, customerPageQueryParams.getStatus());


        IPage<CustomerEntity> iPage = baseMapper.selectPage(page, lambda);
        List<CustomerVO> list = BeanUtil.copyToList(iPage.getRecords(), CustomerVO.class);

        return new PageResult<>(list, page.getTotal(), CustomerVO.class);
    }

    @Override
    public List<CustomerVO> getList(@NotNull CustomerSelectQueryParams customerSelectQueryParams) {
        QueryWrapper<CustomerEntity> wrapper = new QueryWrapper<>();
        return BeanUtil.copyToList(baseMapper.selectList(wrapper), CustomerVO.class);
    }

    @Override
    public void save(@NotNull CustomerAddParams customerAddParams) {
        CustomerEntity customerEntity = BeanUtil.copyProperties(customerAddParams, CustomerEntity.class);
        // 客户编号：KH2025000001   字母固定+年份+6位数字
        customerEntity.setCustomerCode(generateCustomerCode());
        customerEntity.setMaintainingPeople(customerAddParams.getMaintainingPeople().stream().map(Object::toString).collect(Collectors.joining(",")));
        customerEntity.setSharer(customerAddParams.getSharer().stream().map(Object::toString).collect(Collectors.joining(",")));
        baseMapper.insert(customerEntity);
    }


    public String generateCustomerCode() {
        String year = DateUtil.thisYear() + ""; // 获取当前年份，如 20250101
        // 根据当前日期查询数据库最大客户编号
        String maxCustomerCode = baseMapper.selectMaxCustomerCode("KH" + year);
        int nextNum = 1;
        if (maxCustomerCode != null && maxCustomerCode.length() >= 12) {
            String numStr = maxCustomerCode.substring(6, 12);
            try {
                nextNum = Integer.parseInt(numStr) + 1;
            } catch (NumberFormatException e) {
                // 记录日志：编号格式异常
            }
        }
        return "KH" + year + String.format("%06d", nextNum);
    }

    @Override
    public void update(@NotNull CustomerUpdateParams customerUpdateParams) {
        CustomerEntity customerEntity = BeanUtil.copyProperties(customerUpdateParams, CustomerEntity.class);
        customerEntity.setMaintainingPeople(customerUpdateParams.getMaintainingPeople().stream().map(Object::toString).collect(Collectors.joining(",")));
        customerEntity.setSharer(customerUpdateParams.getSharer().stream().map(Object::toString).collect(Collectors.joining(",")));
        baseMapper.updateById(customerEntity);
    }

    @Override
    public CustomerVO getById(Long id) {
        CustomerEntity entity = baseMapper.selectById(id);
        CustomerVO customerVO = BeanUtil.copyProperties(baseMapper.selectById(id), CustomerVO.class);

        customerVO.setMaintainingPeople(Arrays.asList(entity.getMaintainingPeople().split(",")));
        customerVO.setSharer(Arrays.asList(entity.getSharer().split(",")));
        return customerVO;
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