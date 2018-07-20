package com.myrule;

import com.netflix.loadbalancer.IRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 *
 * 自定义负载均衡选择算法
 *
 * 该类不能与主启动类在同一包及其子包下
 */
@Configuration
public class MySelfRule
{
	@Bean
	public IRule myRule()
	{
		//return new RoundRobinRule();// Ribbon默认是轮询
		//return new RandomRule();// Ribbon默认是轮询，我自定义为随机

		return new UserDefinedRule();// 自定义为每台机器5次
	}
}
