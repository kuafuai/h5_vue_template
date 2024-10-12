package com.kuafu.web.handler;

public class TenantContextHolder {
    private static final ThreadLocal<Integer> tenantContext = new ThreadLocal<>();
    private static final boolean enableTenant = false;
    public static final String TENANT_TABLE_FIELD_NAME = "xxxxx";

    public static void setTenant(Integer tenant) {
        tenantContext.set(tenant);
    }

    public static Integer getCurrentTenantId() {
        return tenantContext.get();
    }

    public static void remove() {
        tenantContext.remove();
    }
    public static boolean getEnableTenant() {
        return enableTenant;
    }
}
