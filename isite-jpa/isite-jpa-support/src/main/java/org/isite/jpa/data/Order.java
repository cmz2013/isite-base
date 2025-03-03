package org.isite.jpa.data;

import lombok.Getter;
import org.isite.commons.lang.Assert;
import org.isite.commons.lang.Constants;

/**
 * @Description 数据列表顺序
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
public class Order {

    private String field;
    private Direction direction = Direction.ASC;

    public Order(String field, Direction direction) {
        this.setField(field);
        this.setDirection(direction);
    }

    private void setField(String field) {
        Assert.notBlank(field, "field cannot be blank");
        this.field = field;
    }

    private void setDirection(Direction direction) {
        Assert.notNull(direction, "direction cannot be null");
        this.direction = direction;
    }

    /**
     * 构造排序语句
     */
    public String orderBy() {
        return this.field + Constants.SPACE + this.direction.name();
    }
}
