SpringBoot Web项目接入认证鉴权中心校验token和接口权限控制步骤：
 
    1) 引用Jar包

    <dependency>
       <groupId>org.isite</groupId>
       <artifactId>security-web-starter</artifactId>
    </dependency>

             
    2) 配置接口权限控制，和不需要认证的接口白名单
    
     security:
        oauth2:
          #支持通配符，多个接口逗号分隔
          permit: /api/**
        data:
          #启用数据接口权限控制
          authority: true
          permit: /my/**,/public/**

    3) 校验token
            
    资源服务器调用认证服务器接口校验token和client信息，修改资源服务器的配置文件：
                
    security:
      oauth2:
        resource:
          user-info-uri: http://127.0.0.1:7010/oauth/principal
          # prefer-token-info默认值为true，既优先使用token-info-uri校验token认证信息
          # prefer-token-info设置为false，则会使用user-info-uri，适用于需要获取userdetails信息的场景
          prefer-token-info: false                                                      

三. 资源服务器数据（接口）权限控制 SecurityConstants.PROPERTY_DATA_PERMISSION

    在用户登录的时候，根据（功能权限）授权的资源ID，查询（数据权限）接口路径，缓存到用户登录信息中。
    使用（org.isite.security.filter.DataAuthorityFilter）过滤器拦截请求，
    和当前用户已授权的接口进行匹配，检查当前请求的接口是否被授权。
    
    如果配置文件中未进行属性配置或：security.data.authority=false，则DataAuthorityFilter不生效
    如果要启用数据权限控制，配置该属性为：security.data.authority=true
    
    应用场景：
    1、后台系统用户登录，不同用户访问不同功能（比如基于RBAC功能权限控制），需要做数据接口权限控制
    2、小程序用户登录，但是后台接口面向所有用户都能访问，不需要做数据权限控制

四. 其他注意事项

    1、统一从请求头传递token，不使用url参数传递token，否则TransmittableHeader无法获取token信息
    2、后端服务通过FeignClientFactory获取FeignClient，在头部统一添加自定义的请求头传递token等信息