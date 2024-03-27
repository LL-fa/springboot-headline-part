package com.atguigu.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.atguigu.utils.JwtHelper;
import com.atguigu.utils.MD5Util;
import com.atguigu.utils.Result;
import com.atguigu.utils.ResultCodeEnum;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.pojo.User;
import com.atguigu.service.UserService;
import com.atguigu.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
* @author longfa
* @description 针对表【news_user】的数据库操作Service实现
* @createDate 2023-11-19 14:23:35
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtHelper jwtHelper;
    /**
     * 登录业务实现
     *      1.根据账号，查询用户对象  -loginUser
     *      2.如果用户对象为null，查询失败，账号错误！  给前端返回 501错误
     *      3.对比密码， 若密码错误，失败  返回 503错误
     *      4.根据用户id生成一个 token，   将token 封装到 result 返回
     * @param user
     * @return
     */
    @Override
    public Result login(User user) {
        //根据账号查询数据
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getUsername, user.getUsername());
        User loginUser = userMapper.selectOne(userLambdaQueryWrapper);
        //数据为空 返回501错误
        if (loginUser == null) {
            return Result.build(null, ResultCodeEnum.USERNAME_ERROR);
        }
        //对比密码   密码不为null且密码的MD5加密后的字符串相同说明密码相同
        if (!StringUtils.isEmpty(user.getUserPwd()) && MD5Util.encrypt(user.getUserPwd()).equals(loginUser.getUserPwd())) {
            //密码正确,登录成功

            //登录成功后，要根据用户id生成 token
            String token = jwtHelper.createToken(Long.valueOf(loginUser.getUid()));

            //将token封装到 result返回
            HashMap data = new HashMap();
            data.put("token", token);
            return Result.ok(data);
        }
        //密码错误
        return Result.build(null, ResultCodeEnum.PASSWORD_ERROR);
    }

    /**
     * 根据token获取用户数据
     *
     *  1.token是否在有效期
     *  2.根据token解析userId
     *  3.根据用户id查询数据
     *  4.去掉密码，封装到result结果返回即可
     * @param token
     * @return
     */
    @Override
    public Result getUserInfo(String token) {
        //使用 工具类的isExpiration方法判断 token是否过期
        boolean expiration = jwtHelper.isExpiration(token);
        if (expiration) {
            //true 代表过期. 过期就当作未登录看待
            return Result.build(null, ResultCodeEnum.NOTLOGIN);
        }
        //根据token解析userId
        int userId = jwtHelper.getUserId(token).intValue();

        //根据解析来的userId查询数据     //mybatisPlus的selectById方法有个bug，必须要给主键加上@tableId注解才没有bug
        User user = userMapper.selectById(userId);
        user.setUserPwd("");

        //封装到 result
        HashMap data = new HashMap();
        data.put("loginUser", user);
        return Result.ok(data);
    }

    /**
     * 检查账号是否可用
     *  1.根据账号进行count查询
     *  2. count == 0  可用
     *  3. count > 0 不可用
     * @param username
     * @return
     */
    @Override
    public Result checkUserName(String username) {
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getUsername, username);
        Long count = userMapper.selectCount(userLambdaQueryWrapper);
        if (count > 0) {
            return Result.build(null, ResultCodeEnum.USERNAME_USED);
        }
        return Result.ok(null);
    }

    /**
     * 注册业务
     *  1.检查账号是否已经被注册
     *  2.密码加密处理
     *  3.账号数据保存
     *  4.返回结果
     * @param user
     * @return
     */
    @Override
    public Result regist(User user) {
        //检查账号是否已经被注册
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getUsername, user.getUsername());
        Long count = userMapper.selectCount(userLambdaQueryWrapper);
        if (count > 0) {
            return Result.build(null, ResultCodeEnum.USERNAME_USED);
        }
        //未被注册，直接进行密码加密
        user.setUserPwd(MD5Util.encrypt(user.getUserPwd()));
        //将账号数据添加到数据库
        userMapper.insert(user);
        return Result.ok(null);
    }
}




