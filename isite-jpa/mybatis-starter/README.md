对象关系映射（Object Relational Mapping，简称ORM）模式是一种为了解决面向对象与关系数据库存在的互不匹配的现象的技术。
ORM在业务逻辑层和数据库层之间充当了桥梁的作用。

一、引用Jar包

    <dependency>
       <groupId>org.isite</groupId>
       <artifactId>commons-mybatis</artifactId>
    </dependency>

二、配置yaml

    mybatis:
      #数据对象扫描路径
      type-aliases-package: org.isite.misc.po
      mapper-locations: classpath:mapper/**/*.xml
      #配置数据转换包路径,逗号分隔
      #该配置会被org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration读取,并调用org.mybatis.spring.SqlSessionFactoryBean来完成TypeHandler的初始化
      type-handlers-package: org.isite.commons.mybatis.type
      configuration:
        cache-enabled: true
        #FULL会自动映射任意复杂的结果(嵌套的或其他情况)
        auto-mapping-behavior: FULL
        #REUSE执行器重用预处理语句
        default-executor-type: REUSE
        map-underscore-to-camel-case: true
        lazy-loading-enabled: true
        aggressive-lazy-loading: false
    
    #对通用Mapper进行配置
    mapper:
      #insert和update中，是否判断字符串类型!=''
      not-empty: true
      #取回主键的方式
      identity: MYSQL
      #数据库的catalog，如果设置该值，查询的时候表名会带catalog设置的前缀
      catalog: sys_
      #允许bean接受enum类型
      enum-as-simple-type: true
      #配置JavaBean与数据库表字段映射关系，camelhumpAndLowercase: 驼峰转下划线小写形式
      style: camelhumpAndLowercase

三、在启动类上添加注解 @MapperScan(basePackages = {"org.isite.misc.mapper"})，扫描Mapper