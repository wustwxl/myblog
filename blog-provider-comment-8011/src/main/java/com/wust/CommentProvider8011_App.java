
package com.wust;

import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author F7687967
 * @date 2018-6-25 下午5:22:46
 */

@SpringBootApplication
@EnableEurekaClient //本服务启动后会自动注册进eureka服务中
@EnableDiscoveryClient //服务发现
//@EnableCircuitBreaker //对hystrixR熔断机制的支持
public class CommentProvider8011_App {

	public static void main(String[] args) {
		//常规开启Banner
		//SpringApplication.run(CommentProvider8011_App.class, args);


		//修改Banner的模式为OFF
		SpringApplicationBuilder builder = new SpringApplicationBuilder(CommentProvider8011_App.class);
		builder.bannerMode(Banner.Mode.OFF).run(args);

	}


}
