package org.isite.mongo.converter;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.isite.commons.lang.Constants;
import org.isite.commons.lang.Functions;
import org.isite.commons.lang.Reflection;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class QueryConverter {

    private QueryConverter() {
    }

    /**
     * 转换为Query对象
     */
    public static <P> Query toQuery(Functions<P, Object> getter, Object value) {
        return Query.query(new Criteria().and(Reflection.toFieldName(getter)).is(value));
    }

    public static <P> Query toQuery(Functions<P, Object> getter, Collection<?> values) {
        return Query.query(new Criteria().and(Reflection.toFieldName(getter)).in(values));
    }

    /**
     * 转换为Query对象，使用非null字段作为查询条件
     */
    @SuppressWarnings("unchecked")
    public static Query toQuerySelective(Object object, String... ignores) {
        Criteria criteria = new Criteria();
        if (null != object) {
            List<Field> fields = Reflection.getFields(object.getClass());
            if (CollectionUtils.isEmpty(fields)) {
                return Query.query(criteria);
            }
            List<String> ignoreList = ArrayUtils.isNotEmpty(ignores) ? Arrays.asList(ignores) : Collections.emptyList();
            fields.forEach(field -> {
                if (ignoreList.contains(field.getName())) {
                    return;
                }
                Object value = Reflection.getValue(object, field.getName());
                if (null == value || Constants.BLANK_STR.equals(value)) {
                    return;
                }
                criteria.and(field.getName()).is(value);
            });
        }
        return Query.query(criteria);
    }
}
