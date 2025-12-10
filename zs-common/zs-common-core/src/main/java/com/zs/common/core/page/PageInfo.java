package com.zs.common.core.page;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zs.common.core.constant.Constants;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

/**
 * @author zsadmin
 */
public class PageInfo<T> extends Page<T> {

    public PageInfo(@NotNull Map<String, Object> params) {
        long page = Long.parseLong(params.getOrDefault(Constants.PAGE, "0").toString());
        long limit = Long.parseLong(params.getOrDefault(Constants.SIZE, "10").toString());
        String order = params.getOrDefault(Constants.ORDER, "").toString();
        String orderField = params.getOrDefault(Constants.ORDER_FIELD, "").toString();

        this.setCurrent(page);
        this.setSize(limit);

        if (Constants.ASC.equalsIgnoreCase(order)) {
            this.addOrder(OrderItem.asc(StrUtil.toUnderlineCase(orderField)));
        } else if (Constants.DESC.equalsIgnoreCase(order)) {
            this.addOrder(OrderItem.desc(StrUtil.toUnderlineCase(orderField)));
        }
    }

    public PageInfo(@NotNull BasePageParams basePageParams) {

        this.setCurrent(basePageParams.getCurrent());
        this.setSize(basePageParams.getPageSize());

        if (Constants.ASC.equalsIgnoreCase(basePageParams.getOrder())) {
            this.addOrder(OrderItem.asc(StrUtil.toUnderlineCase(basePageParams.getOrderField())));
        } else if (Constants.DESC.equalsIgnoreCase(basePageParams.getOrder())) {
            this.addOrder(OrderItem.desc(StrUtil.toUnderlineCase(basePageParams.getOrderField())));
        }
    }
}
