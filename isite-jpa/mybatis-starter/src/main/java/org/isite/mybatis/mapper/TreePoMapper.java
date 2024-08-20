package org.isite.mybatis.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.UpdateProvider;
import org.isite.mybatis.data.TreePo;
import tk.mybatis.mapper.annotation.RegisterMapper;

/**
 * @Description 通用Mapper配置参数mapper.mappers，是我们定义Mapper时需要实现的接口。
 * 注解@RegisterMapper可以避免mapper.mappers参数配置，通用Mapper检测到该接口被继承时，会自动注册
 * @Author <font color='blue'>zhangcm</font>
 */
@RegisterMapper
public interface TreePoMapper<T extends TreePo<I>, I> extends PoMapper<T, I> {
    /**
     * 使用右模糊，更新指定分支节点下的所有节点的pids
     * @param setPids pids字段value表达式（根据数据库类型，使用不同的函数设置pids的值）
     * @param likePids like查询条件
     * @return 更新条数
     */
    @UpdateProvider(type = TreePidsProvider.class, method = "updatePids")
    Integer updatePids(@Param("setPids") String setPids, @Param("likePids") String likePids);
}
