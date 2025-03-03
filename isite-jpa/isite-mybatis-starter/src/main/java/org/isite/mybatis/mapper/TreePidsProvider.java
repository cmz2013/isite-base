package org.isite.mybatis.mapper;

import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class TreePidsProvider extends MapperTemplate {

    public TreePidsProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    public String updatePids(MappedStatement mappedStatement) {
        Class<?> entityClass = this.getEntityClass(mappedStatement);
        return SqlHelper.updateTable(entityClass, this.tableName(entityClass)) +
                updateSetColumns() +
                whereColumns();
    }

    private String updateSetColumns() {
        return  "<set>" +
                "pid = ${setPids}" +
                "</set> ";
    }

    private String whereColumns() {
        return  "<where>" +
                "pids like #{likePids}" +
                "</where>";
    }
}
