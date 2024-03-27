package com.atguigu.controller;

import com.atguigu.mapper.HeadlineMapper;
import com.atguigu.pojo.Headline;
import com.atguigu.service.HeadlineService;
import com.atguigu.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * ClassName: HeadLineController
 * Package: com.atguigu.controller
 * Description:
 *
 * @Author:
 * @Create: 2023/11/20 - 17:22
 * @Version: v1.0
 */
@RestController
@RequestMapping("headline")
@CrossOrigin
public class HeadLineController {

    @Autowired
    private HeadlineService headlineService;

    //这个方法需要登录之后才能访问，
    @PostMapping("publish")
    public Result publish(@RequestBody Headline headline, @RequestHeader String token) {
        Result result = headlineService.publish(headline, token);
        return result;
    }

    //这里也有拦截保护，只要在headline路径下的方法都有拦截保护
    @PostMapping("findHeadlineByHid")
    public Result findHeadlineByHid(Integer hid) {
        //因为service层使用了 mybatisPlus增强，所以mapper有的方法service也有
        Headline headline = headlineService.getById(hid);
        HashMap hashMap = new HashMap();
        hashMap.put("headline", headline);
        return Result.ok(hashMap);
    }

    @PostMapping("update")
    public Result update(@RequestBody Headline headline) {
        Result result = headlineService.updateData(headline);
        return result;
    }

    @PostMapping("removeByHid")
    public Result removeByHid(Integer hid) {
        boolean b = headlineService.removeById(hid);
        return Result.ok(null);
    }
}
