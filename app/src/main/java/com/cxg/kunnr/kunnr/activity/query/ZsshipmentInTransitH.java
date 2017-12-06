package com.cxg.kunnr.kunnr.activity.query;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Description: 在途运单信息汇总实例
 * author: xg.chen
 * time: 2017/11/22
 * version: 1.0
 */
@DatabaseTable(tableName = "ZsshipmentInTransitH")
public class ZsshipmentInTransitH  implements Serializable {

    @DatabaseField
    private String tknum;//装运编号
    @DatabaseField
    private String kunnr;//送达方
    @DatabaseField
    private String name1;//名称 1
    @DatabaseField
    private String kunag;//售达方
    @DatabaseField
    private String name2;//名称 2
    @DatabaseField
    private String ydhrq;//应到货日期
    @DatabaseField
    private String lfimg1;//成品（FERT）
    @DatabaseField
    private String lfimg2;//辅料（非FERT）

    public String getTknum() {
        return tknum;
    }

    public void setTknum(String tknum) {
        this.tknum = tknum;
    }

    public String getKunnr() {
        return kunnr;
    }

    public void setKunnr(String kunnr) {
        this.kunnr = kunnr;
    }

    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public String getKunag() {
        return kunag;
    }

    public void setKunag(String kunag) {
        this.kunag = kunag;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    public String getYdhrq() {
        return ydhrq;
    }

    public void setYdhrq(String ydhrq) {
        this.ydhrq = ydhrq;
    }

    public String getLfimg1() {
        return lfimg1;
    }

    public void setLfimg1(String lfimg1) {
        this.lfimg1 = lfimg1;
    }

    public String getLfimg2() {
        return lfimg2;
    }

    public void setLfimg2(String lfimg2) {
        this.lfimg2 = lfimg2;
    }

    @Override
    public String toString() {
        return "ZsshipmentInTransitH{" +
                "tknum='" + tknum + '\'' +
                ", kunnr='" + kunnr + '\'' +
                ", name1='" + name1 + '\'' +
                ", kunag='" + kunag + '\'' +
                ", name2='" + name2 + '\'' +
                ", ydhrq='" + ydhrq + '\'' +
                ", lfimg1='" + lfimg1 + '\'' +
                ", lfimg2='" + lfimg2 + '\'' +
                '}';
    }
}
