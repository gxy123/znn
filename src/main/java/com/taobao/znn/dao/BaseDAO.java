package com.taobao.znn.dao;


import com.taobao.znn.bean.BaseDO;
import com.taobao.znn.bean.BaseQueryDO;

import java.util.List;

public interface BaseDAO<T extends BaseDO, Q extends BaseQueryDO> {
    long save(T var1);

    long batchSave(List<T> var1);

    long update(T var1);

    long remove(Long var1);

    int queryCount(Q var1) throws Exception;

    List<T> queryList(Q var1) throws Exception;

    T queryObject(Long var1) throws Exception;
}
