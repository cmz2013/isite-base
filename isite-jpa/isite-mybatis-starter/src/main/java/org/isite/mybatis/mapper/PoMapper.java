package org.isite.mybatis.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.isite.mybatis.data.Po;
import tk.mybatis.mapper.annotation.RegisterMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.special.InsertListMapper;

/**
 * @Description
 * 1）通用Mapper配置参数 mapper.mappers，是我们定义 Mapper 时需要继承的接口。
 * 注解 @RegisterMapper 可以避免 mapper.mappers 参数配置，通用 Mapper 检测到该接口被继承时，会自动注册。
 * 2）在Mapper接口中，不支持方法重载（方法名相同，参数类型不同），即xml文件中，sql语句的id必须唯一。
 * @Author <font color='blue'>zhangcm</font>
 */
@RegisterMapper
public interface PoMapper<T extends Po<I>, I> extends Mapper<T>, InsertListMapper<T> {
    /**
     * 根据非空字段判断记录是否存在
     * 注解@SelectProvider 用于动态生成SQL查询语句。它允许你使用一个类和方法来生成SQL字符串，而不是在注解中直接编写SQL。
     */
    @SelectProvider(type = PoExistsProvider.class, method = "existsByPo")
    boolean existsByPo(@Param("po") T po);

    /**
     * 根据非空字段判断记录是否存在，排除指定ID
     */
    @SelectProvider(type = PoExistsProvider.class, method = "existsByPoAndExcludeId")
    boolean existsByPoAndExcludeId(@Param("po") T po, @Param("excludeId") I excludeId);

    /**
     * 根据指定字段判断记录是否存在
     */
    @SelectProvider(type = PoExistsProvider.class, method = "existsByColumn")
    boolean existsByColumn(@Param("column") String column, @Param("value") Object value);

    /**
     * 根据指定字段判断记录是否存在，排除指定ID
     */
    @SelectProvider(type = PoExistsProvider.class, method = "existsByColumnAndExcludeId")
    boolean existsByColumnAndExcludeId(@Param("column") String column, @Param("value") Object value, @Param("excludeId") I excludeId);
}
