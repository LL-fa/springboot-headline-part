package com.atguigu.test;

import com.atguigu.utils.JwtHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * ClassName: JwtTest
 * Package: com.atguigu.test
 * Description:
 *
 * @Author:
 * @Create: 2023/11/19 - 14:55
 * @Version: v1.0
 */
@SpringBootTest
public class JwtTest {

    @Autowired
    private JwtHelper jwtHelper;

    @Test
    public void test(){
        //生成 传入用户标识
        String token = jwtHelper.createToken(1L);
        System.out.println("token = " + token);

        //解析用户标识
        int userId = jwtHelper.getUserId(token).intValue();
        System.out.println("userId = " + userId);

        //校验是否到期! false 未到期 true到期
        boolean expiration = jwtHelper.isExpiration(token);
        System.out.println("expiration = " + expiration);

        System.out.println("expiration2 = " + expiration);
        System.out.println("expiration3 = " + expiration);
        System.out.println("expiration4 = " + expiration);
        System.out.println("master-test");
        System.out.println("fix-hot");
        System.out.println("push commit github");
        System.out.println("pull commit github");
    }
}
