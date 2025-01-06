#数据集成管理平台开发步骤

###1、引用jar包
```    
<dependency>
    <groupId>org.isite</groupId>
    <artifactId>data-starter</artifactId>
</dependency>
```    
###2、添加配置
```
isite: 
  data:
    admin:
      addresses: http://isite-dev.com:7050
    
#执行器接口签名私钥
security:
  signature:
    userSecret:
      #执行器调用数据管理平台接口的签名密码
      data-admin: xxx
      #执行器调用远程服务接口的签名密码
      api-${id}: xxx

#配置Email，发送数据接口失败告警信息（如果不引用commons-email，不需要配置email，不会发送告警信息）
email:
  smtp:
    host: smtp.10086.cn
    port: 465
    timeout: 60000
  from:
    address: itcto@139.com
    name: 项目管理平台
    authentication:
      username: itcto@139.com
      password: ENC(ij46DqIoc+m9jT8MfbwaWg==)
  ssl: true
  freemarker:
    tempalte-loader-path:
```
