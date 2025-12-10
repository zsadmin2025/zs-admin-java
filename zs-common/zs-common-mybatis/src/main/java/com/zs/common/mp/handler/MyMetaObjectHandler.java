package com.zs.common.mp.handler;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.zs.common.core.tenant.TenantContext;
import com.zs.common.core.utils.SecurityUtil;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

/**
 * 公共字段填充
 *
 * @author zsadmin
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "creator", Long.class, SecurityUtil.getUserInfo().getSysUser().getSysUserId());
        this.strictInsertFill(metaObject, "createTime", String.class, DateUtil.now());
        // 租户id
        this.strictInsertFill(metaObject, "tenantId", String.class, TenantContext.getTenantId());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "updater", Long.class, SecurityUtil.getUserInfo().getSysUser().getSysUserId());
        this.strictUpdateFill(metaObject, "updateTime", String.class, DateUtil.now());
    }
}
