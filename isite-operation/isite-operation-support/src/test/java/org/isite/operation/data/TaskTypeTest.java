package org.isite.operation.data;

import org.isite.commons.lang.json.Jackson;
import org.isite.operation.support.enums.EventType;
import org.isite.operation.support.enums.TaskType;
/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class TaskTypeTest {

    public static void main(String[] args) {
        System.out.println(Jackson.toJsonString(TaskType.values(EventType.POST_SHOP_PAYMENT_NOTIFY)));
    }
}
