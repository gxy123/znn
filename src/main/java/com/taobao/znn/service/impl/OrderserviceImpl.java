package com.taobao.znn.service.impl;

import com.taobao.znn.bean.OrderDO;
import com.taobao.znn.bean.OrderQueryDO;
import com.taobao.znn.dao.OrderDao;
import com.taobao.znn.service.Orderservice;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @ClassName OrderserviceImpl
 * @Author guoxiaoyu
 * @Date 2019/12/1314:48
 **/
@Service
public class OrderserviceImpl implements Orderservice {
    @Resource
    private OrderDao orderDao;

    @Override
    public List<OrderDO> getList(OrderQueryDO queryDO) throws Exception {
        return orderDao.queryList(queryDO);
    }

    @Override
    public Long add(OrderDO orderDO) throws Exception {
        if (Objects.isNull(orderDO)) {
            throw new Exception("参数为null！");
        }
        long save;
        if (Objects.isNull(orderDO.getId())) {
            save = orderDao.save(orderDO);
        } else {
            save = orderDao.update(orderDO);
        }

        return save;
    }
}
