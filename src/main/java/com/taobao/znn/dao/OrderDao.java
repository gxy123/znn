package com.taobao.znn.dao;


import com.taobao.znn.bean.OrderDO;
import com.taobao.znn.bean.OrderQueryDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 
 *
 * @author guoxiaoyu
 * @email ggg_xiaoyu@163.com
 * @date 2019-09-26 15:46:48
 */
@Mapper
public interface OrderDao extends BaseDAO<OrderDO, OrderQueryDO> {

}
