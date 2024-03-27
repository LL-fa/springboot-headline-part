package com.atguigu.service.impl;

import com.atguigu.pojo.vo.PortalVo;
import com.atguigu.utils.JwtHelper;
import com.atguigu.utils.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.pojo.Headline;
import com.atguigu.service.HeadlineService;
import com.atguigu.mapper.HeadlineMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @author longfa
* @description 针对表【news_headline】的数据库操作Service实现
* @createDate 2023-11-19 14:23:35
*/
@Service
public class HeadlineServiceImpl extends ServiceImpl<HeadlineMapper, Headline>
    implements HeadlineService{

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private HeadlineMapper headlineMapper;

    /**
     * 首页数据查询
     *  1.进行分页数据查询
     *  2.分页数据，拼接到 result即可
     *
     *  注意1： 查询需要自定义语句  自定义mapper的方法，携带分页
     *  注意2：返回的结果是 List<Map>
     * @param portalVo
     * @return
     */
    @Override
    public Result findNewsPage(PortalVo portalVo) {
        Page<Map> mapPage = new Page<>(portalVo.getPageNum(), portalVo.getPageSize());
        headlineMapper.selectMyPage(mapPage, portalVo);
        //获取得到的结果都封装到 mapPage中
        List<Map> records = mapPage.getRecords();
        Map pageData = new HashMap<>();
        pageData.put("pageData", records);
        pageData.put("pageNum", mapPage.getCurrent());
        pageData.put("pageSize", mapPage.getSize());
        pageData.put("totalPage", mapPage.getPages());
        pageData.put("totalSize", mapPage.getTotal());
        Map pageInfo = new HashMap<>();
        pageInfo.put("pageInfo",pageData);
        return Result.ok(pageInfo);
    }

    /**
     * 根据id查询详情
     *  2.查询对应的数据即可 【多表查询，需要头条和用户表，mybatisPlus只支持单表查询，这里需要自定义，返回类型map】
     *  1.修改阅读量 + 1 【version乐观锁，当前数据对应的版本】
     * @param hid
     * @return
     */
    @Override
    public Result showHeadlineDetail(Integer hid) {
        Map data = headlineMapper.queryDetailMap(hid);
        Map headlineMap = new HashMap();
        headlineMap.put("headline", data);

        //修改阅读量 + 1
        Headline headline = new Headline();
        headline.setHid((Integer) data.get("hid"));
        headline.setVersion((Integer) data.get("version"));
        //阅读量 + 1
        headline.setPageViews((Integer) data.get("pageViews") + 1);

        headlineMapper.updateById(headline);
        return Result.ok(headlineMap);
    }

    /**
     * 发布头条的方法
     *  1.补全 headline对象的数据
     * @param headline
     * @param token
     * @return
     */
    @Override
    public Result publish(Headline headline, String token) {
        //解析token，获取用户id
        int userId = jwtHelper.getUserId(token).intValue();
        //数据装配
        //发布者id
        headline.setPublisher(userId);
        headline.setPageViews(0);
        headline.setCreateTime(new Date());
        headline.setUpdateTime(new Date());

        headlineMapper.insert(headline);
        return Result.ok(null);
    }

    /**
     * 头条修改
     *  头条修改要注意两个点：
     *      1.根据传入的hid 查询该条数据的最新version
     *      2.修改数据的时间为 现在更新的时间
     * @param headline
     * @return
     */
    @Override
    public Result updateData(Headline headline) {
        //先查询最新的version
        Integer version = headlineMapper.selectById(headline.getHid()).getVersion();
        //将最新的version赋给headline  【乐观锁】
        headline.setVersion(version);
        //修改headline的时间为当前时间
        headline.setUpdateTime(new Date());

        //将修改后的headline添加到数据库
        headlineMapper.updateById(headline);
        return Result.ok(null);
    }

}




