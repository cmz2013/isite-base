package org.isite.operation.data;

import org.isite.commons.lang.Reflection;
import org.isite.commons.lang.json.Jackson;
import org.isite.operation.support.vo.SignScoreProperty;
/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class TaskPropertyTest {

    public static void main(String[] args) {
        System.out.println(Jackson.toJsonString(Reflection.toJsonFields(SignScoreProperty.class)));
    }
}
