package com.atguigu.pojo.vo;

import lombok.Data;

/**
 * ClassName: PortalVo
 * Package: com.atguigu.pojo.vo
 * Description: vo类的使用情况，在没有任何类可以接值时，可以创建vo类
 *
 * @Author:
 * @Create: 2023/11/19 - 19:55
 * @Version: v1.0
 */
@Data
public class PortalVo {

    private String keyWords;
    private Integer type = 0;
    private int pageNum = 1;
    private int pageSize = 10;
}
