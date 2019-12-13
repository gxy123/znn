package com.taobao.znn.bean;

import lombok.Data;

import java.util.Date;

/**
 * @author guoxiaoyu
 * @email ggg_xiaoyu@163.com
 * @date 2019-09-26 15:46:48
 */
@Data

public class OrderDO extends BaseDO {

    private static final long serialVersionUID = 1L;


    private Long orderId;

    private Long id;

    private String name;

    private Long danJia;

    private Long count;

    private Long danJiaAll;

    private Long cbDanJia;

    private Long cbDanJiaAll;

    private String product;

    private Date date;

    private String remark;
}
