package com.taobao.znn.service;

import com.taobao.znn.bean.OrderDO;
import com.taobao.znn.bean.OrderQueryDO;

import java.util.List;

public interface Orderservice {

   List<OrderDO> getList(OrderQueryDO queryDO) throws Exception;
   Long add(OrderDO orderDO) throws Exception;

}
