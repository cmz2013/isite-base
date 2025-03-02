package org.isite.mybatis.data;

import lombok.Getter;
import lombok.Setter;
import org.isite.jpa.data.Model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

/**
 * @Description PO用于Service层、DAO层
 * 1、实体字段值映射到数据库字段，采用驼峰字段映射
 * 2、主键字段使用@Id注解
 * 3、非数据库字段使用@Transient标注
 * 4、枚举类型使用@ColumnType注解标注；并指明Handler处理器
 * 5、字段类型要求使用引用类型，用于非空字段的查询、更新等操作，也可以防止查询结果转基本类型时出现空指针.
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class Po<I> implements Model<I> {
    /**
     * 主键，使用JDBC的getGeneratedKeys方法来取出由数据库内部生成的主键
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    private I id;

    /**
     * 创建时间
     */
    @Column(insertable = false, updatable = false)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Column(insertable = false, updatable = false)
    private LocalDateTime updateTime;
}
