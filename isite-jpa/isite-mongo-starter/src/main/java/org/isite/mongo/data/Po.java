package org.isite.mongo.data;

import lombok.Getter;
import lombok.Setter;
import org.isite.jpa.data.Model;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

/**
 * @Description
 * 1）PO用于Service层、DAO层。
 * 2）字段类型要求使用引用类型，用于非空字段的查询、更新等操作，也可以防止查询结果转基本类型时出现空指针.
 * 3）使用@Field注解，可指定存储的键值名称，默认就是类字段名。
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class Po<I> implements Model<I> {
    /**
     * 1) _id字段是为主键保留的，默认类型是ObjectId
     * 2) ObjectId 是一个12字节 BSON 类型数据，有以下格式：
     * 前4个字节表示时间戳
     * 接下来的3个字节是机器标识码
     * 紧接的两个字节由进程id组成（PID）
     * 最后三个字节是随机数
     * 3) mongodb可以将ObjectId类型转为String，但不能转为Long或Integer等类型
     * 4) 加@Id注解可自定义主键类型（可以是任何类型）以及自定义自增规则，mongo库主键字段还是为"_id"
     */
    @Id
    private I id;
    /**
     * 创建时间
     * 针对@CreatedDate注解，创建时会自动赋值，需要在配置类中添加 @EnableMongoAuditing 注解使其生效
     */
    @CreatedDate
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    @LastModifiedDate
    private LocalDateTime updateTime;
}