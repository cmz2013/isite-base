package org.isite.jpa.data;

/**
 * @Description 数据模型(PO)父接口
 * @Author <font color='blue'>zhangcm</font>
 */
public interface Model<I> {
    /**
     * 获取主键
     */
    I getId();

    void setId(I id);
}
