package org.isite.commons.web.ribbon;

import com.netflix.loadbalancer.IRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Configuration
public class RibbonConfig {

	@Bean
	public IRule grayRule() {
		return new GrayRule();
	}
}
