package com.wust;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer // EurekaServer服务器端启动类,接受其它微服务注册进来
public class EurekaServer7001_App
{
	public static void main(String[] args)
	{

		//常规开启Banner
		//SpringApplication.run(EurekaServer7001_App.class, args);


		//修改Banner的模式为OFF
		SpringApplicationBuilder builder = new SpringApplicationBuilder(EurekaServer7001_App.class);
		builder.bannerMode(Banner.Mode.OFF).run(args);
	}
}
