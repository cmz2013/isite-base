SpringCloud Gateway项目接入认证鉴权中心校验token和接口权限控制步骤：
 
    1) 引用Jar包

    <dependency>
       <groupId>org.isite</groupId>
       <artifactId>security-gateway-starter</artifactId>
    </dependency>
             
    2) 配置接口权限控制，和不需要认证的接口白名单
    
     security:
        oauth2:
          #支持通配符，多个接口逗号分隔
          permit: /api/**,/oauth/**
        data:
          #启用数据接口权限控制
          authority: true
          permit: /my/**,/public/**

    3) 校验token
            
    网关服务器调用认证服务器接口校验token和client信息，修改配置文件：
                
    security:
      oauth2:
        resource:
          user-info-uri: http://127.0.0.1:7010/oauth/user

三. 资源服务器数据（接口）权限控制 SecurityConstants.PROPERTY_DATA_PERMISSION

    在用户登录的时候，根据（功能权限）授权的资源ID，查询（数据权限）接口路径，缓存到用户登录信息中。
    使用（org.isite.security.filter.DataAuthorityFilter）过滤器拦截请求，
    和当前用户已授权的接口进行匹配，检查当前请求的接口是否被授权。
    
    如果配置文件中未进行属性配置或：security.data.authority=false，则DataAuthorityFilter不生效
    如果要启用数据权限控制，配置该属性为：security.data.authority=true
    
    应用场景：
    1、后台系统用户登录，不同用户访问不同功能（比如基于RBAC功能权限控制），需要做数据接口权限控制
    2、小程序用户登录，但是后台接口面向所有用户都能访问，不需要做数据权限控制

四. 在使用网关转发请求时，注意事项

    1、/oauth 是认证鉴权中心内置和自定义接口的前缀，网关不能截掉前缀。
    2、网关转发请求时添加host请求头（API签名信息，API token信息，授权码登录须要传递session信息）
    3、API网关是对外提供服务的统一出入口（外网），校验token和API权限
    4、后端服务在内网部署，通过服务发现机制来查找服务实例，并进行负载均衡。后端服务不校验token和API权限
    5、所有资源服务器的API白名单，统一在网关维护，为了防止越来越复杂、难以维护、网关负载重，url约定以下命名规则：
       /api/**：api打头的接口不校验token和接口权限。
       /my/**: my打头的接口不校验接口权限（当前用户只能访问自己的数据），但需要校验token才可以访问
       /public/**: 公共接口不校验接口权限（所有用户都能访问的数据），但需要校验token才可以访问

       注意：
       1）/api/**、/my/**、/public/**这三种接口不需要在rbac data_api表中进行配置
       2）因为在网关统一进行接口权限校验，调用链路只会校验第一个接口的权限，所以，不需要在rbac resource_api表中配置调用链路的第二个及后续的接口

    6、/oauth/**接口在网关中不做token校验和接口权限控制。认证鉴权中心（公共接口）会对token进行校验，网关只负责转发请求。
       如果其他后端服务也需要接入认证鉴权中心，进行token校验和接口权限控制，那么需要在网关配置白名单，不做token校验和接口权限控制。        

    7、如果服务提供的都是公共接口，可以不用/data、/my打头，比如：
        security-center,在数据接口权限白名单中可以用 /oauth/**
        user-center,在数据接口权限白名单中可以用 /user/**
    8、网关校验token通过后，会在请求头添加当前登录的用户ID、员工ID、租户ID
        
五. 其他注意事项

    1、统一从请求头传递token，不使用url参数传递token，否则TransmittableHeader无法获取token信息
    2、后端服务通过FeignClientFactory获取FeignClient，在头部统一添加自定义的请求头传递token等信息