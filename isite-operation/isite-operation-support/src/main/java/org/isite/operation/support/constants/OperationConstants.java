package org.isite.operation.support.constants;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class OperationConstants {

    private OperationConstants() {
    }

    /**
     * 服务ID
     */
    public static final String SERVICE_ID = "isite-operation-admin";

    public static final String FIELD_ACTIVITY_ID = "activityId";
    public static final String FIELD_ACTIVITY_PID = "activityPid";
    public static final String FIELD_RECEIVE_STATUS = "receiveStatus";
    /**
     * 请求参数：邀请码
     */
    public static final String FIELD_INVITE = "invite";
    public static final String FIELD_TOTAL_INVENTORY = "totalInventory";
    public static final String FIELD_EVENT_PARAM = "eventParam";
    /**
     * MQ队列：运营任务事件消息队列
     */
    public static final String QUEUE_OPERATION_EVENT = "operation-event";
}
