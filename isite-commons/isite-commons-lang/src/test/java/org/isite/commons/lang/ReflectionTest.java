package org.isite.commons.lang;

import lombok.Getter;
import lombok.Setter;
/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class ReflectionTest {

    private String data;

    public String testFieldName(Functions<ReflectionTest, String> getter) {
        return Reflection.toFieldName(getter);
    }

    public static void main(String[] args) {
        System.out.println(new ReflectionTest().testFieldName(ReflectionTest::getData));
    }
}
