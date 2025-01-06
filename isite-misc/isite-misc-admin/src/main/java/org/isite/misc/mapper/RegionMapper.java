package org.isite.misc.mapper;

import org.isite.misc.po.RegionPo;
import org.isite.mybatis.mapper.TreePoMapper;
import org.springframework.stereotype.Repository;

/**
 * @Description 地区DAO
 * 1、定义的DAO需要实现Mapper接口
 * 2、@Mapper注解是Mybatis的注解，和Spring没有关系。在DAO接口上@Mapper一定要有，否则Mybatis找不到。
 * 3、在启动类或配置类上添加@MapperScan注解，可以替代@Mapper
 * 4、@Repository是Spring的注解，用于声明一个Bean。@Repository可有可无，加上可以消去IDEA依赖注入的报错信息。
 * @Author <font color='blue'>zhangcm</font>
 */
@Repository
public interface RegionMapper extends TreePoMapper<RegionPo, Integer> {
}
