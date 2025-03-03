package org.isite.mongo.converter;

import org.isite.commons.lang.Reflection;
import org.springframework.data.mongodb.core.query.Update;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class UpdateConverter {
    private UpdateConverter() {
    }

    /**
     * 转换为Update对象
     */
    public static Update toUpdate(Object object, String... ignores) {
        List<Field> fields = Reflection.getFields(object.getClass());
        Update update = new Update();
        List<String> ignoreList = ignores != null ? Arrays.asList(ignores) : Collections.emptyList();
        for (Field field : fields) {
            if (ignoreList.contains(field.getName())) {
                continue;
            }
            update.set(field.getName(), Reflection.getValue(object, field.getName()));
        }
        return update;
    }

    /**
     * 转换为Update对象，忽略null字段
     */
    public static Update toUpdateSelective(Object object, String... ignores) {
        List<Field> fields = Reflection.getFields(object.getClass());
        Update update = new Update();
        List<String> ignoreList = ignores != null ? Arrays.asList(ignores) : Collections.emptyList();
        for (Field field : fields) {
            if (ignoreList.contains(field.getName())) {
                continue;
            }
            Object value = Reflection.getValue(object, field.getName());
            if (null != value) {
                update.set(field.getName(), value);
            }
        }
        return update;
    }
}
