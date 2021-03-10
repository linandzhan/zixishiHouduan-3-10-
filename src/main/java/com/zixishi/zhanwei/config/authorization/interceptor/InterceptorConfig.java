package com.zixishi.zhanwei.config.authorization.interceptor;


import com.zixishi.zhanwei.config.authorization.resolvers.CurrentUserMethodArgumentResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Resource
    private Environment environment;


    @Resource
    private CurrentUserMethodArgumentResolver currentUserMethodArgumentResolver;
//    private String attachmentHome;
//    @PostConstruct
//    public void init() {
//        //上传路径
//        this.attachmentHome = (String) this.environment.getProperty("attachment.home", String.class, "attachment/");
//        System.out.println(attachmentHome);
//        if (this.attachmentHome != null && !this.attachmentHome.endsWith("/")) {
//            attachmentHome = attachmentHome.replace(".", "/");
//            System.out.println(attachmentHome);
//        }
//    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor())
                .addPathPatterns("/**");        //拦截所有请求，通过判断是否有@LoginRequired 注解 决定是否需要登录
    }

    @Bean
    public HandlerInterceptor authenticationInterceptor() {
        return new AuthorizationInterceptor();
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("*")
                .allowedHeaders("*");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //http://localhost:8888/image   /test.jpg
        registry.addResourceHandler("/attachment/**").addResourceLocations("file:"+ "D:/myproject/MyBiYeSheJi2/src/main/java/com/zixishi/zhanwei/config/image/");
    }


    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(currentUserMethodArgumentResolver);
    }




}
