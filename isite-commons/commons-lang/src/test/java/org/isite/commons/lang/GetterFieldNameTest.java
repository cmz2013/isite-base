package org.isite.commons.lang;

import lombok.Getter;
import lombok.Setter;

import static org.isite.commons.lang.Reflection.toFieldName;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class GetterFieldNameTest {

    private String data;

    public String test(Functions<GetterFieldNameTest, String> getter) {
        return toFieldName(getter);
    }

    public static void main(String[] args) {
        System.out.println(new GetterFieldNameTest().test(GetterFieldNameTest::getData));
    }
}
