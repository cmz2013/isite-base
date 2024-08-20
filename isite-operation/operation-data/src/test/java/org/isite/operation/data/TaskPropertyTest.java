package org.isite.operation.data;

import org.isite.operation.data.vo.SignScoreProperty;

import static org.isite.commons.lang.Reflection.toJsonFields;
import static org.isite.commons.lang.json.Jackson.toJsonString;

/**
 * @author <font color='blue'>zhangcm</font>
 */
public class TaskPropertyTest {

    public static void main(String[] args) {
        System.out.println(toJsonString(toJsonFields(SignScoreProperty.class)));
    }
}
