package com.cay.Model.File.entity;

import com.cay.Model.BaseEntity;

/**
 * Created by 陈安一 on 2017/1/22.
 */
public class FileEntity extends BaseEntity {
    private String url;

    private long totalCount;

    private long totalPage;

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public long getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(long totalPage) {
        this.totalPage = totalPage;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
