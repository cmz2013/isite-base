package org.isite.operation.po;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.cloud.enums.TerminalType;
import org.isite.mybatis.data.Po;
import org.isite.mybatis.type.EnumTypeHandler;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.Table;

/**
 * @Description 活动网页模板
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
@Table(name = "webpage")
public class WebpagePo extends Po<Integer> {
    /**
     * 运营活动id
     */
    private Integer activityId;
    /**
     * 用户终端类型
     */
    @ColumnType(typeHandler = EnumTypeHandler.class)
    private TerminalType terminalType;
    /**
     * 源码（thymeleaf模板页面）
     */
    private String code;
}
