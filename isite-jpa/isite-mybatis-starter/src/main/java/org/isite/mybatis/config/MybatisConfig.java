package org.isite.mybatis.config;

import org.isite.mybatis.service.TreePidsGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.isite.commons.lang.Constants.COMMA;
import static org.isite.commons.lang.Constants.ONE;
import static org.isite.jpa.data.JpaConstants.FIELD_PIDS;
import static org.springframework.boot.jdbc.DatabaseDriver.MYSQL;
import static org.springframework.boot.jdbc.DatabaseDriver.ORACLE;

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
            if (MYSQL.name().equals(productName) || ORACLE.name().equals(productName)) {
                return (newPids, oldPids) -> "concat('" + newPids +
                        //sbstr截取字符串下标从1开始，如果是0则返回空
                        "', substr(" + FIELD_PIDS + COMMA + (oldPids.length() + ONE) + "))";
            }
            return null;
        }
    }
}
