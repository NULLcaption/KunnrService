package com.cxg.kunnr.kunnr.activity.query;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Description: POD确认到货值文件上传实体类
 * author: xg.chen
 * time: 2017/11/28
 * version: 1.0
 */
@DatabaseTable(tableName = "KunnrPodFile")
public class KunnrPodFile implements Serializable {

    @DatabaseField
    private String attId;//序列号
    @DatabaseField
    private String attFileName;//附件描述
    @DatabaseField
    private String attUrl;//文件路径
    @DatabaseField
    private String createDate;//创建时间
    @DatabaseField
    private String otherName;//附件别名(系统给的附件名字,按序列号+第几次创建)
    @DatabaseField
    private String status;//状态Y/N

    public KunnrPodFile(String attId, String attFileName, String attUrl, String createDate, String otherName, String status) {
        this.attId = attId;
        this.attFileName = attFileName;
        this.attUrl = attUrl;
        this.createDate = createDate;
        this.otherName = otherName;
        this.status = status;
    }

    public KunnrPodFile() {
        super();
    }

    public String getAttId() {
        return attId;
    }

    public void setAttId(String attId) {
        this.attId = attId;
    }

    public String getAttFileName() {
        return attFileName;
    }

    public void setAttFileName(String attFileName) {
        this.attFileName = attFileName;
    }

    public String getAttUrl() {
        return attUrl;
    }

    public void setAttUrl(String attUrl) {
        this.attUrl = attUrl;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getOtherName() {
        return otherName;
    }

    public void setOtherName(String otherName) {
        this.otherName = otherName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "KunnrPodFile{" +
                "attId='" + attId + '\'' +
                ", attFileName='" + attFileName + '\'' +
                ", attUrl='" + attUrl + '\'' +
                ", createDate='" + createDate + '\'' +
                ", otherName='" + otherName + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
