package com.zs.common.mp.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.zs.common.mp.handler.MultiTenantHandler;
import com.zs.common.mp.handler.MyDataPermissionHandler;
import com.zs.common.mp.interceptor.MyDataPermissionInterceptor;
import com.zs.common.mp.properties.TenantProperties;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zsadmin
 */
@Configuration
@EnableConfigurationProperties(TenantProperties.class)
public class MybatisPlusConfig {


    @NotNull
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(TenantProperties tenantProperties) {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        if (Boolean.TRUE.equals(tenantProperties.getEnable())) {
            // 启用多租户插件拦截
            interceptor.addInnerInterceptor(new TenantLineInnerInterceptor(new MultiTenantHandler(tenantProperties)));
        }


        // 乐观锁插件
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());

        // 防止全表更新和删除
        interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());


        // 数据权限插件
        MyDataPermissionInterceptor myDataPermissionInterceptor = new MyDataPermissionInterceptor();
        myDataPermissionInterceptor.setDataPermissionHandler(new MyDataPermissionHandler());
        interceptor.addInnerInterceptor(myDataPermissionInterceptor);



        // 分页插件
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());

        return interceptor;
    }




}
