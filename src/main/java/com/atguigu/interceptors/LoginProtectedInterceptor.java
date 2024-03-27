package com.atguigu.interceptors;

import com.atguigu.utils.JwtHelper;
import com.atguigu.utils.Result;
import com.atguigu.utils.ResultCodeEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * ClassName: LoginProtectedInterceptor
 * Package: com.atguigu.interceptors
 * Description:登录保护 拦截器，检查请求头是否包含有效的token
 *      包含有效的请求头，放行
 *      没有包含请求头 或 请求头无效，不放行，返回504
 *
 * @Author:
 * @Create: 2023/11/20 - 17:46
 * @Version: v1.0
 */
@Component
public class LoginProtectedInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtHelper jwtHelper;

    //处理器执行之前
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //从请求头中获取token
        String token = request.getHeader("token");
        //检查token是否有效
        boolean expiration = jwtHelper.isExpiration(token);
        //有效放行
        if (!expiration) {
            return true;
        }

        //无效返回504状态的 json,不放行
        Result<Object> result = Result.build(null, ResultCodeEnum.NOTLOGIN);
        //将result 转为json字符串
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(result);
        response.getWriter().print(json);

        return false;
    }
}
