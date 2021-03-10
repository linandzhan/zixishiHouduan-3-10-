package com.zixishi.zhanwei.config.authorization.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
// 标注这个注解的注解保留时期
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiredPermission {
    String value() default "path";
}
