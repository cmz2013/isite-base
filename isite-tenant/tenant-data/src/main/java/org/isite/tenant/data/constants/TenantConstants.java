package org.isite.tenant.data.constants;

/**
 * @author <font color='blue'>zhangcm</font>
 */
public class TenantConstants {

    private TenantConstants() {
    }

    /**
     * 服务ID
     */
    public static final String SERVICE_ID = "tenant-admin";
    /**
     * 超级管理员角色
     */
    public static final String ROLE_ADMINISTRATOR = "Administrator";
    /**
     * 功能权限产品供应商
     */
    public static final String SPU_SUPPLIER_TENANT_RESOURCE = "tenant-resource";
    /**
     * 队列：订单支付成功-资源（功能权限）
     */
    public static final String QUEUE_TRADE_ORDER_SUCCESS_TENANT_RESOURCE = "trade-order-success-tenant-resource";
}
