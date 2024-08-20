package org.isite.mongo.converter;

import lombok.SneakyThrows;
import org.isite.commons.lang.Functions;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.lang.reflect.Field;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.EMPTY_LIST;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.apache.commons.lang3.ArrayUtils.isNotEmpty;
import static org.isite.commons.lang.Reflection.getFields;
import static org.isite.commons.lang.Reflection.getValue;
import static org.isite.commons.lang.Reflection.toFieldName;
import static org.isite.commons.lang.data.Constants.BLANK;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class QueryConverter {

    private QueryConverter() {
    }

    /**
     * 转换为Query对象
     */
    @SneakyThrows
    public static <P> Query toQuery(Functions<P, Object> getter, Object value) {
        Criteria criteria = new Criteria();
        criteria.and(toFieldName(getter)).is(value);
        return query(criteria);
    }

    /**
     * 转换为Query对象，使用非null字段作为查询条件
     */
    @SuppressWarnings("unchecked")
    public static Query toQuerySelective(Object object, String... ignores) {
        Criteria criteria = new Criteria();
        if (null != object) {
            List<Field> fields = getFields(object.getClass());
            if (isEmpty(fields)) {
                return query(criteria);
            }

            List<String> ignoreList = isNotEmpty(ignores) ? asList(ignores) : EMPTY_LIST;
            fields.forEach(field -> {
                if (ignoreList.contains(field.getName())) {
                    return;
                }
                Object value = getValue(object, field.getName());
                if (null == value || BLANK.equals(value)) {
                    return;
                }
                criteria.and(field.getName()).is(value);
            });
        }
        return query(criteria);
    }
}
