package org.isite.commons.lang.data;

import lombok.Getter;
import lombok.Setter;

import static org.isite.commons.lang.Assert.notBlank;
import static org.isite.commons.lang.data.Direction.ASC;

/**
 * @Description 数据列表顺序
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class Order {

    private String property;
    private Direction direction = ASC;

    public Order() {
    }

    public Order(String property) {
        this.property = property;
    }

    public Order(String property, Direction direction) {
        this.property = property;
        this.direction = direction;
    }

    /**
     * 构造sql字符串
     */
    public String getOrderBy() {
        notBlank(property, "property must be set");
        return this.property + " " + this.direction.name();
    }
}
