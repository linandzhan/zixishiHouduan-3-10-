package com.zixishi.zhanwei.config.swagger;

import io.swagger.annotations.Api;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

// JAVA代码配置的方式 和 xml的方式本质一样
@Configuration
@EnableSwagger2
public class SwaggerConfig {

	// 类似 xml <bean ...>
	@Bean
	public ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("后端管理系统的接口").description("介绍了 controller相关接口的信息").version("1.0").build();
	}

	@Bean
	public Docket createApi() {
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select()
				// 扫描 哪些controller接口需要生成这个swagger文档和测试页面
				.apis(RequestHandlerSelectors.withClassAnnotation(Api.class)).paths(PathSelectors.any()).build();
	}

}
