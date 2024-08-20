通过控制台将规则写入到配置中心，客户端监听规则变化，更新到本地缓存时规则生效。

在SpringCloud Gateway项目中集成 Sentinel 的操作步骤：

1、pom.xml添加maven依赖

        <dependency>
            <groupId>org.isite</groupId>
            <artifactId>sentinel-gateway-starter</artifactId>
        </dependency>
        
2、bootstrap.yml添加配置

    spring:
      cloud:
        nacos:
          config:
            extension-configs:
              -  group: DEFAULT_GROUP
                 data‐id: sentinel-gateway.yml


3、sentinel-gateway.yml 配置文件内容

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
                data-id: ${spring.application.name}-sentinel-gw-api-group
                namespace: 00000001-0000-0000-0000-000000000000
                group-id: SENTINEL_GROUP
                rule-type: gw-api-group
                data-type: json
            - nacos:
                server-addr: 127.0.0.1:8848
                username: nacos
                password: nacos
                data-id: ${spring.application.name}-sentinel-gw-flow
                namespace: 00000001-0000-0000-0000-000000000000
                group-id: SENTINEL_GROUP
                rule-type: gw-flow
                data-type: json
            