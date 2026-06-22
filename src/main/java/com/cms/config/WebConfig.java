package com.cms.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.cms.util.RoleInterceptor;

@Configuration
public class WebConfig
		implements WebMvcConfigurer
{

	@Autowired
	private RoleInterceptor roleInterceptor;

	@Override
	public void addInterceptors(
			InterceptorRegistry registry)
	{
		registry.addInterceptor(roleInterceptor)
				.addPathPatterns("/admin/**",
								 "/employee/**");
	}
}