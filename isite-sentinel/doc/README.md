### Hystrix vs Sentinel

    Hystrix 的关注点在于以隔离和熔断为主的容错机制，超时或被熔断的调用将会快速失败，并可以提供 fallback 机制。
    而 Sentinel 的侧重点在于：多样化的流量控制、熔断降级、系统负载保护、实时监控和控制台

### Sentinel 二次开发

    1. 规则配置使用nacos数据源进行持久化（否则，这些规则仅在内存，应用重启之后，该规则会丢失）
    2. 为springboot项目定义starter，完成自动装配： sentine-starter
       
