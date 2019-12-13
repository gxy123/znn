package com.taobao.znn.bean;

import java.beans.Transient;

/**
 * @ClassName BaseQueryDO
 * @Author guoxiaoyu
 * @Date 2019/9/2710:58
 **/
public class BaseQueryDO {

    private String verify;

    private String ticket;

    private Integer pageNum;

    private Integer pageSize;

    public BaseQueryDO() {
    }

    public Integer getPageNum() {
        if (this.pageNum == null) {
            this.pageNum = 0;
        }

        return this.pageNum;
    }

    public void setPageNum(Integer pageNum) {
        if (pageNum != null && pageNum < 0) {
            this.pageNum = 0;
        } else {
            this.pageNum = pageNum;
        }

    }

    public Integer getPageSize() {
        if (this.pageSize == null || this.pageSize <= 0) {
            this.pageSize = 20;
        }

        return this.pageSize;
    }

    public void setPageSize(Integer pageSize) {
        if (pageSize != null && pageSize > 0) {
            this.pageSize = pageSize;
        } else {
            this.pageSize = 20;
        }

    }

    @Transient
    public Integer getFirstRow() {
        return this.getPageNum() * this.getPageSize();
    }

    public String getVerify() {
        return this.verify;
    }

    public void setVerify(String verify) {
        this.verify = verify;
    }

    public String getTicket() {
        return this.ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }
}
