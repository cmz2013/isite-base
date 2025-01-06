Spring Bean内部方法调用

    在使用Spring AOP的时候，我们从IOC容器中获取的Bean对象其实都是代理对象，而不是那些Bean对象本身，
    由于this关键字引用的并不是该Bean对象的代理对象，而是其本身，因此AOP是不能拦截到类内部自调用的方法。
    
    解决方案：
    修改类，不要出现自调用的情况：这是Spring文档中推荐的最佳方案；
    若一定要使用自调用，那么this.doSomething()替换为：((CustomerService) AopContext.currentProxy()).doSomething()；
    此时需要修改spring的aop配置：<aop:aspectj-autoproxy expose-proxy="true" />
    
    SpringBoot使用@EnableAspectJAutoProxy注解开启AspectJ自动代理技术，也可以在appllication.properties中进行配置:
    spring.aop.auto=true
    # 开启CGLIB代理
    spring.aop.proxy-target-class=true