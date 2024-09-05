package org.isite.commons.web.feign;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClientBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import static org.isite.commons.lang.Constants.DOT;

/**
 * @Description Feign客户端工厂类，动态指定服务ID，手动构建Feign客户端。
 * 在内部服务之间调用时，通过FeignClientFactory获取Feign客户端。
 * Feign的构建器添加的请求拦截器，会在请求头统一添加自定义信息,并且拦截器的作用范围是该Feign客户端的所有请求。
 * 第三方应用，可以使用注解@FeignClient(url)定义客户端，避免泄露关键信息。
 * Feign客户端有两个重要的属性：name(value) 和 url：
 * name用于指定服务名称（service ID），feign客户端会通过服务发现机制来查找服务实例，并进行负载均衡；
 * url用于指定服务的地址，不会进行服务发现和负载均衡。
 * @Author <font color='blue'>zhangcm</font>
 */
@Component
public class FeignClientFactory {

    /**
     * FeignClientBuilder和@FeignClient的功效是一样的
     */
    private final FeignClientBuilder feignClientBuilder;

    @Autowired
    public FeignClientFactory(ApplicationContext context) {
        this.feignClientBuilder = new FeignClientBuilder(context);
    }

    /**
     * @Description 获取FeignClient。
     * @param tClass FeignClient接口类
     * @param name 用于指定服务名称（service ID）,Feign客户端会通过服务发现机制来查找服务实例，并进行负载均衡。
     */
    public <T> T getFeignClient(final Class<T> tClass, String name) {
        FeignClientBuilder.Builder<T> builder = this.feignClientBuilder.forType(tClass, name);
        builder.contextId(name + DOT + tClass.getSimpleName());
        builder.customize(feignBuilder -> feignBuilder.requestInterceptor(new HeaderEnhancer()));
       return builder.build();
    }
}
