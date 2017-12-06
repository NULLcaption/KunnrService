package com.cxg.kunnr.kunnr.activity.query;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Description: POD确认单明细
 * author: xg.chen
 * time: 2017/11/28
 * version: 1.0
 */
@DatabaseTable(tableName = "KunnrPodDetail")
public class KunnrPodDetail implements Serializable {
    @DatabaseField
    private String waybillId;//运单号
    @DatabaseField
    private String mvgr1;// 品项
    @DatabaseField
    private String lfimg;//数量
    @DatabaseField
    private String unit;//单位
    @DatabaseField
    private String items;//行项目
    @DatabaseField
    private String id;//序列号
    @DatabaseField
    private String count;//明细数量

    public KunnrPodDetail() {
        super();
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getWaybillId() {
        return waybillId;
    }

    public void setWaybillId(String waybillId) {
        this.waybillId = waybillId;
    }

    public String getMvgr1() {
        return mvgr1;
    }

    public void setMvgr1(String mvgr1) {
        this.mvgr1 = mvgr1;
    }

    public String getLfimg() {
        return lfimg;
    }

    public void setLfimg(String lfimg) {
        this.lfimg = lfimg;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
