package com.wust;

import com.myrule.MySelfRule;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;

@SpringBootApplication
@EnableEurekaClient
//无数据库操作
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
// 在启动该微服务的时候就能去加载我们的自定义Ribbon配置类，从而使配置生效
// MySelfRule该类不能与主启动类在同一包及其子包下
@RibbonClient(name="BLOG-CONTENT",configuration=MySelfRule.class)
public class Consumer80_App
{
	public static void main(String[] args)
	{

		//常规开启Banner
		//SpringApplication.run(Consumer80_App.class, args);


		//修改Banner的模式为OFF
		SpringApplicationBuilder builder = new SpringApplicationBuilder(Consumer80_App.class);
		builder.bannerMode(Banner.Mode.OFF).run(args);
	}
}
