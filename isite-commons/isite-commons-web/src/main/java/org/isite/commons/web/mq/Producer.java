package org.isite.commons.web.mq;
/**
 * @Description 消息生产接口
 * @Author <font color='blue'>zhangcm</font>
 */
public interface Producer {
    /**
     * 创建消体
     * @param args 方法入参
     * @param returnValue 方法返回值
     * @return 消息数据
     */
    Object getBody(Object[] args, Object returnValue);
}
