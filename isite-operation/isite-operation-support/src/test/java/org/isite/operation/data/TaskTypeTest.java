package org.isite.operation.data;

import org.isite.operation.support.enums.TaskType;

import static org.isite.commons.lang.json.Jackson.toJsonString;
import static org.isite.operation.support.enums.EventType.POST_SHOP_PAY_NOTIFY;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class TaskTypeTest {

    public static void main(String[] args) {
        System.out.println(toJsonString(TaskType.values(POST_SHOP_PAY_NOTIFY)));
    }
}
