package org.sr.activiti.bean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@PropertySource("classpath:alfresco.properties")
public class ApplicationConf {

	@Bean
	public static PropertySourcesPlaceholderConfigurer getProperties() {
		return new PropertySourcesPlaceholderConfigurer();
	}
}