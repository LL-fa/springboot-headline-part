package com.atguigu.service;

import com.atguigu.pojo.Headline;
import com.atguigu.pojo.vo.PortalVo;
import com.atguigu.utils.Result;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author longfa
* @description 针对表【news_headline】的数据库操作Service
* @createDate 2023-11-19 14:23:35
*/
public interface HeadlineService extends IService<Headline> {

    /**
     * 首页数据查询
     * @param portalVo
     * @return
     */
    Result findNewsPage(PortalVo portalVo);

    /**
     * 根据id查询头条详情
     * @param hid
     * @return
     */
    Result showHeadlineDetail(Integer hid);

    /**
     * 头条发布
     * @param headline
     * @return
     */
    Result publish(Headline headline, String token);

    /**
     * 修改头条
     * @param headline
     * @return
     */
    Result updateData(Headline headline);
}
