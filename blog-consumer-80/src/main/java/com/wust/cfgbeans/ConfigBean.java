package com.wust.cfgbeans;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RetryRule;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ConfigBean //boot -->spring   applicationContext.xml --- @Configuration配置   ConfigBean = applicationContext.xml
{ 
	@Bean
	@LoadBalanced //Spring Cloud Ribbon是基于Netflix Ribbon实现的一套客户端 负载均衡的工具。
	public RestTemplate getRestTemplate()
	{

		return new RestTemplate();
	}


	@Bean
	public IRule myRule()
	{
		//return new RoundRobinRule(); //默认的轮询算法
		//return new RandomRule(); //随机算法
		return new RetryRule(); // 先轮询,若某一服务失败，切换至其他可用服务。
	}

}

// applicationContext.xml == ConfigBean(@Configuration)
//<bean id="userServcie" class="com.atguigu.tmall.UserServiceImpl">