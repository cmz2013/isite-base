通过控制台将规则写入到配置中心，客户端监听规则变化，更新到本地缓存时规则生效。

在spring-boot web 项目中集成 Sentinel 的操作步骤：

1、pom.xml添加maven依赖

        <dependency>
            <groupId>org.isite</groupId>
            <artifactId>sentinel-web-starter</artifactId>
        </dependency>

2、bootstrap.yml添加配置

    spring:
      cloud:
        nacos:
          config:
            extension-configs:
              -  group: DEFAULT_GROUP
                 data‐id: isite-sentinel-web.yml

             
3、isite-sentinel-web.yml 配置文件内容

    feign:
      sentinel:
        enabled: true
    
    spring:
      cloud:
        sentinel:
          enabled: true
          #关闭懒加载，体现在项目启动后dashboard就能看到
          eager: true
          transport:
            dashboard: 127.0.0.1:7060
          datasource:
            - nacos:
                server-addr: 127.0.0.1:8848
                username: nacos
                password: nacos
                data-id: ${spring.application.name}-sentinel-flow
                namespace: 00000001-0000-0000-0000-000000000000
                group-id: SENTINEL_GROUP
                rule-type: flow
                data-type: json
            - nacos:
                server-addr: 127.0.0.1:8848
                username: nacos
                password: nacos
                data-id: ${spring.application.name}-sentinel-degrade
                namespace: 00000001-0000-0000-0000-000000000000
                group-id: SENTINEL_GROUP
                rule-type: degrade
                data-type: json
            - nacos:
                server-addr: 127.0.0.1:8848
                username: nacos
                password: nacos
                data-id: ${spring.application.name}-sentinel-system
                namespace: 00000001-0000-0000-0000-000000000000
                group-id: SENTINEL_GROUP
                rule-type: system
                data-type: json
            - nacos:
                server-addr: 127.0.0.1:8848
                username: nacos
                password: nacos
                data-id: ${spring.application.name}-sentinel-param-flow
                namespace: 00000001-0000-0000-0000-000000000000
                group-id: SENTINEL_GROUP
                rule-type: param-flow
                data-type: json
            - nacos:
                server-addr: 127.0.0.1:8848
                username: nacos
                password: nacos
                data-id: ${spring.application.name}-sentinel-authority
                namespace: 00000001-0000-0000-0000-000000000000
                group-id: SENTINEL_GROUP
                rule-type: authority
                data-type: json

