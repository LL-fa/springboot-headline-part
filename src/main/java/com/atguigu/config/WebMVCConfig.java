package com.atguigu.config;

import com.atguigu.interceptors.LoginProtectedInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * ClassName: WebMVCConfig
 * Package: com.atguigu.config
 * Description:
 *
 * @Author:
 * @Create: 2023/11/20 - 18:01
 * @Version: v1.0
 */
@Configuration
public class WebMVCConfig implements WebMvcConfigurer {

    //因为LoginProtectedInterceptor类放入ioc容器了，所以可以这样获取
    @Autowired
    private LoginProtectedInterceptor loginProtectedInterceptor;

    //添加拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
                                                          //添加要拦截的路径
        registry.addInterceptor(loginProtectedInterceptor).addPathPatterns("/headline/**");
    }


}
