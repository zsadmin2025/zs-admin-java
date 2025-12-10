package com.zs.common.core.tenant;

/**
 * 租户上下文，用于在当前线程中存储和获取租户ID
 */
public class TenantContext {
    private static final ThreadLocal<String> CURRENT_TENANT = new ThreadLocal<>();

    /**
     * 设置当前租户ID
     */
    public static void setTenantId(String tenantId) {
        CURRENT_TENANT.set(tenantId);
    }

    /**
     * 获取当前租户ID
     */
    public static String getTenantId() {
        return CURRENT_TENANT.get();
    }

    /**
     * 清除当前线程的租户ID
     */
    public static void clear() {
        CURRENT_TENANT.remove();
    }
}