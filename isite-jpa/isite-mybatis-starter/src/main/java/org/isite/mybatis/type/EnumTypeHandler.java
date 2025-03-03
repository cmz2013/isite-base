package org.isite.mybatis.type;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.isite.commons.lang.Reflection;
import org.isite.commons.lang.enums.Enumerable;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * @Description 枚举类型处理，枚举类实现接口Enumerable，数据库存储编码
 * @Author <font color='blue'>zhangcm</font>
 */
public class EnumTypeHandler extends BaseTypeHandler<Enumerable<?>> {
    /**
     * 枚举编码数据类型
     */
    private Class<?> codeClass;
    /**
     * 枚举类
     */
    private Class<? extends Enumerable<?>> enumType;

    public EnumTypeHandler() {
        super();
    }

    /**
     * 设置转换类
     */
    public EnumTypeHandler(Class<? extends Enumerable<?>> enumType) {
        this.codeClass = Reflection.getGenericParameter(enumType, Enumerable.class);
        this.enumType = enumType;
    }

    /**
     * 对PreparedStatement设值，从java类型到数据库类型映射
     */
    @Override
    public void setNonNullParameter(
            PreparedStatement ps, int i, Enumerable<?> parameter, JdbcType jdbcType) throws SQLException {
        ps.setObject(i, parameter.getCode());
    }

    /**
     * 从ResultSet获取值，从数据库到java类型映射
     */
    @Override
    public Enumerable<?> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return Enumerable.getByCode(enumType, rs.getObject(columnName, codeClass));
    }

    @Override
    public Enumerable<?> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return Enumerable.getByCode(enumType, rs.getObject(columnIndex, codeClass));
    }

    @Override
    public Enumerable<?> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return Enumerable.getByCode(enumType, cs.getObject(columnIndex, codeClass));
    }
}
