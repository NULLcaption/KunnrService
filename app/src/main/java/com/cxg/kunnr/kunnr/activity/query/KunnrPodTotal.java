package com.cxg.kunnr.kunnr.activity.query;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Description: POD到货确认总单
 * author: xg.chen
 * time: 2017/11/28
 * version: 1.0
 */
@DatabaseTable(tableName = "KunnrPodTotal")
public class KunnrPodTotal implements Serializable {

    @DatabaseField
    private String waybillId;//运单号
    @DatabaseField
    private String kunnr;//经销商
    @DatabaseField
    private String estimateDate;//预计到货日期(应到货日期)
    @DatabaseField
    private String abnormal;//异常分类
    @DatabaseField
    private String arrivalDate;//到货时间
    @DatabaseField
    private String remark;//备注
    @DatabaseField
    private String status;//回单状态
    @DatabaseField
    private String createDate;//创建时间
    @DatabaseField
    private String lastModDate;//最后一次修改时间
    @DatabaseField
    private String creator;//创建人
    @DatabaseField
    private String modifier;//修改人
    @DatabaseField
    private String flag;//状态
    @DatabaseField
    private String id;//序列号
    @DatabaseField
    private String type;//提报方式(PC/M)
    @DatabaseField
    private String address;//详细地址
    @DatabaseField
    private String longitude;//经度
    @DatabaseField
    private String latitude;//纬度

    public KunnrPodTotal() {
        super();
    }

    public KunnrPodTotal(String waybillId, String kunnr, String estimateDate, String abnormal, String arrivalDate, String remark, String status, String createDate, String lastModDate, String creator, String modifier, String flag, String id, String type, String address, String longitude, String latitude) {
        this.waybillId = waybillId;
        this.kunnr = kunnr;
        this.estimateDate = estimateDate;
        this.abnormal = abnormal;
        this.arrivalDate = arrivalDate;
        this.remark = remark;
        this.status = status;
        this.createDate = createDate;
        this.lastModDate = lastModDate;
        this.creator = creator;
        this.modifier = modifier;
        this.flag = flag;
        this.id = id;
        this.type = type;
        this.address = address;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getWaybillId() {
        return waybillId;
    }

    public void setWaybillId(String waybillId) {
        this.waybillId = waybillId;
    }

    public String getKunnr() {
        return kunnr;
    }

    public void setKunnr(String kunnr) {
        this.kunnr = kunnr;
    }

    public String getEstimateDate() {
        return estimateDate;
    }

    public void setEstimateDate(String estimateDate) {
        this.estimateDate = estimateDate;
    }

    public String getAbnormal() {
        return abnormal;
    }

    public void setAbnormal(String abnormal) {
        this.abnormal = abnormal;
    }

    public String getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(String arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getLastModDate() {
        return lastModDate;
    }

    public void setLastModDate(String lastModDate) {
        this.lastModDate = lastModDate;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        return "KunnrPodTotal{" +
                "waybillId='" + waybillId + '\'' +
                ", kunnr='" + kunnr + '\'' +
                ", estimateDate='" + estimateDate + '\'' +
                ", abnormal='" + abnormal + '\'' +
                ", arrivalDate='" + arrivalDate + '\'' +
                ", remark='" + remark + '\'' +
                ", status='" + status + '\'' +
                ", createDate='" + createDate + '\'' +
                ", lastModDate='" + lastModDate + '\'' +
                ", creator='" + creator + '\'' +
                ", modifier='" + modifier + '\'' +
                ", flag='" + flag + '\'' +
                ", id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", address='" + address + '\'' +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                '}';
    }
}
