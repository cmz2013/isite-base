package org.isite.mybatis.config;

import org.isite.commons.lang.Constants;
import org.isite.jpa.data.JpaConstants;
import org.isite.mybatis.service.TreePidsGenerator;
import org.springframework.boot.jdbc.DatabaseDriver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Configuration
public class MybatisConfig {

    /**
     * 注册 PidsGenerator实例
     */
    @Bean
    public TreePidsGenerator pidsGenerator(DataSource dataSource) throws SQLException {
        //自动识别数据库类型
        try(Connection connection = dataSource.getConnection()) {
            String productName = connection.getMetaData().getDatabaseProductName().toUpperCase();
            if (DatabaseDriver.MYSQL.name().equals(productName) || DatabaseDriver.ORACLE.name().equals(productName)) {
                //sbstr截取字符串下标从1开始，如果是0则返回空
                return (newPids, oldPids) -> "concat('" + newPids + "', substr(" +
                        JpaConstants.FIELD_PIDS + Constants.COMMA + (oldPids.length() + Constants.ONE) + "))";
            }
            return null;
        }
    }
}
