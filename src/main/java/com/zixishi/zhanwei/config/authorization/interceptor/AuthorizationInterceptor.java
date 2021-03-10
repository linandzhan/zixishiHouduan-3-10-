package com.zixishi.zhanwei.config.authorization.interceptor;


import com.zixishi.zhanwei.config.authorization.annotation.Authorization;
import com.zixishi.zhanwei.config.authorization.annotation.RequiredPermission;
import com.zixishi.zhanwei.config.authorization.token.TokenManager;
import com.zixishi.zhanwei.config.authorization.token.TokenModel;
import com.zixishi.zhanwei.model.Account;
import com.zixishi.zhanwei.model.Permission;
import com.zixishi.zhanwei.service.AccountService;
import com.zixishi.zhanwei.service.PermissionService;
import com.zixishi.zhanwei.service.RoleService;
import com.zixishi.zhanwei.service.impl.PermissionServiceImpl;
import com.zixishi.zhanwei.util.Constants;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * 自定义拦截器，判断此次请求是否有权限
 * @see
 * @author zwl
 */
@Component
public class AuthorizationInterceptor implements HandlerInterceptor {

    @Resource
    private TokenManager manager;
    @Resource
    private PermissionService permissionService;
    @Resource
    private RoleService roleService;
    @Resource
    private AccountService accountService;

    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        //如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        String requestURI = request.getRequestURI();
        System.out.println(requestURI);

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        //从header中得到token
        String authorization = request.getHeader(Constants.AUTHORIZATION);
        //验证token
        TokenModel model = manager.getToken(authorization);

        //如果验证token失败，并且方法注明了Authorization，返回401错误
        if (method.getAnnotation(Authorization.class) != null) {
            if (manager.checkToken(model)) {
                //如果token验证成功，将token对应的用户id存在request中，便于之后注入
                request.setAttribute(Constants.CURRENT_USER_ID, model.getId());
                //根据角色查询权限，符合权限的放行
                // 验证权限
                if (this.hasPermission(request,method,model)) {
                    return true;
                }else {
                    returnJson(response,"{\"code\":400,\"msg\":\"当前用户没有权限访问\"}");
                }
            }
            returnJson(response,"{\"code\":400,\"msg\":\"您没有权限访问，请登录\"}");
            return false;
        }
        return true;
    }






    /**
     * 是否有权限
     */
    private boolean hasPermission(HttpServletRequest request,Method method,TokenModel model) {

//            // 获取方法上的注解
            RequiredPermission requiredPermission = method.getAnnotation(RequiredPermission.class);
            // 如果方法上的注解为空 则获取类的注解
            if (requiredPermission == null) {
                requiredPermission = method.getDeclaringClass().getAnnotation(RequiredPermission.class);
            }
            // 如果注解为null, 说明不需要拦截, 直接放过
            if (requiredPermission == null) {
                return true;
            }

            // 如果标记了注解，则判断权限
            if (StringUtils.isNotBlank(requiredPermission.value())) {
                String path = request.getRequestURI();
                Boolean free = permissionService.findByPath(path);
                if(free) {
                    //对所有开放
                    return true;
                }

                long id = model.getId();
                Set<String> permissionSet = permissionService.findByUser(id);
                if(CollectionUtils.isEmpty(permissionSet)) {
                    return  false;
                }
                return permissionSet.contains(requiredPermission.value());
            }
        return true;
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("ppp");
    }
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("aaaa");
    }



    private void returnJson(HttpServletResponse response, String json) throws Exception{
        PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=utf-8");
        try {
            writer = response.getWriter();
            writer.print(json);

        } catch (IOException e) {
        } finally {
            if (writer != null)
                writer.close();
        }
    }
//    private FullHttpResponse writeText(HttpVersion httpVersion, int code, String text) throws IOException {
//        return this.write(httpVersion, code, text.getBytes(StandardCharsets.UTF_8), "text/plain");
//    }
}
