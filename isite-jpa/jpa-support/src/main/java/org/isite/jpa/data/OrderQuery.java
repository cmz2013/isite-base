package org.isite.jpa.data;

import lombok.Getter;

import static org.isite.commons.lang.Assert.notBlank;
import static org.isite.commons.lang.Assert.notNull;
import static org.isite.commons.lang.Constants.SPACE;
import static org.isite.jpa.data.Direction.ASC;

/**
 * @Description 数据列表顺序
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
public class OrderQuery {

    private String field;
    private Direction direction = ASC;

    public OrderQuery(String field) {
        this.setField(field);
    }

    public OrderQuery(String field, Direction direction) {
        this.setField(field);
        this.setDirection(direction);
    }

    private void setField(String field) {
        notBlank(field, "field cannot be blank");
        this.field = field;
    }

    private void setDirection(Direction direction) {
        notNull(direction, "direction cannot be null");
        this.direction = direction;
    }

    /**
     * 构造排序语句
     */
    public String orderBy() {
        return this.field + SPACE + this.direction.name();
    }
}
