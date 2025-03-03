package org.isite.mybatis.mapper;

import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;
/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class PoExistsProvider extends MapperTemplate {

    private static final String LIMIT_ONE = " LIMIT 1";
    private static final String SELECT_ONE = "SELECT 1";
    private static final String SELECT_EXISTS = "SELECT EXISTS (%s)";

    public PoExistsProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    public String existsByPo(MappedStatement mappedStatement) {
        Class<?> entityClass = this.getEntityClass(mappedStatement);
        return String.format(SELECT_EXISTS, SELECT_ONE +
                SqlHelper.fromTable(entityClass, this.tableName(entityClass)) +
                SqlHelper.whereAllIfColumns(entityClass, this.isNotEmpty()) +
                LIMIT_ONE);
    }

    public String existsByPoAndExcludeId(MappedStatement mappedStatement) {
        Class<?> entityClass = this.getEntityClass(mappedStatement);
        return String.format(SELECT_EXISTS, SELECT_ONE +
                SqlHelper.fromTable(entityClass, this.tableName(entityClass)) +
                SqlHelper.whereAllIfColumns(entityClass, this.isNotEmpty()) + "AND id!= #{excludeId}" +
                LIMIT_ONE);
    }

    public String existsByColumn(MappedStatement mappedStatement) {
        Class<?> entityClass = this.getEntityClass(mappedStatement);
        return String.format(SELECT_EXISTS, SELECT_ONE +
                SqlHelper.fromTable(entityClass, this.tableName(entityClass)) +
                "<where>${column} = #{value}</where>" +
                LIMIT_ONE);
    }

    public String existsByColumnAndExcludeId(MappedStatement mappedStatement) {
        Class<?> entityClass = this.getEntityClass(mappedStatement);
        return String.format(SELECT_EXISTS, SELECT_ONE +
                SqlHelper.fromTable(entityClass, this.tableName(entityClass)) +
                "<where>${column} = #{value} AND id!= #{excludeId}</where>" +
                LIMIT_ONE);
    }
}
