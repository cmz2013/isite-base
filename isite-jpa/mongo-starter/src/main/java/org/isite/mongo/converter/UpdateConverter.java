package org.isite.mongo.converter;

import org.springframework.data.mongodb.core.query.Update;

import java.lang.reflect.Field;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.isite.commons.lang.Reflection.getFields;
import static org.isite.commons.lang.Reflection.getValue;

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
        List<Field> fields = getFields(object.getClass());
        Update update = new Update();
        List<String> ignoreList = ignores != null ? asList(ignores) : emptyList();
        for (Field field : fields) {
            if (ignoreList.contains(field.getName())) {
                continue;
            }
            update.set(field.getName(), getValue(object, field.getName()));
        }
        return update;
    }

    /**
     * 转换为Update对象，忽略null字段
     */
    public static Update toUpdateSelective(Object object, String... ignores) {
        List<Field> fields = getFields(object.getClass());
        Update update = new Update();
        List<String> ignoreList = ignores != null ? asList(ignores) : emptyList();
        for (Field field : fields) {
            if (ignoreList.contains(field.getName())) {
                continue;
            }
            Object value = getValue(object, field.getName());
            if (null != value) {
                update.set(field.getName(), value);
            }
        }
        return update;
    }
}
