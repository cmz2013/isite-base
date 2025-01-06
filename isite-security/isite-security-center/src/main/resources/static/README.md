网关转发请求时，/oauth 打头的请求路径，不截掉前缀。
静态资源通过网关转发请求，也须要添加前缀：/oauth

eg: /oauth/css/login.css