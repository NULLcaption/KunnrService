package com.cxg.kunnr.kunnr.activity.query;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Description: 在途运单明细实例
 * author: xg.chen
 * time: 2017/11/23
 * version: 1.0
 */
@DatabaseTable(tableName = "ZsshipmentInTransit")
public class ZsshipmentInTransit implements Serializable {

    @DatabaseField
    private String tknum;
    @DatabaseField
    private String kunnr;
    @DatabaseField
    private String name1;
    @DatabaseField
    private String kunag;
    @DatabaseField
    private String name2;
    @DatabaseField
    private String ydhrq;
    @DatabaseField
    private String matnr;
    @DatabaseField
    private String maktx;
    @DatabaseField
    private String lfimg;
    @DatabaseField
    private String vrkme;
    @DatabaseField
    private String mtart;
    @DatabaseField
    private String mtbez;
    @DatabaseField
    private String wbstk;
    @DatabaseField
    private String pdstk;
    @DatabaseField
    private String wadatIst;
    @DatabaseField
    private String stras;
    @DatabaseField
    private String mvgr1;
    @DatabaseField
    private String unit;

    public String getMvgr1() {
        return mvgr1;
    }

    public void setMvgr1(String mvgr1) {
        this.mvgr1 = mvgr1;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

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

    public String getMatnr() {
        return matnr;
    }

    public void setMatnr(String matnr) {
        this.matnr = matnr;
    }

    public String getMaktx() {
        return maktx;
    }

    public void setMaktx(String maktx) {
        this.maktx = maktx;
    }

    public String getLfimg() {
        return lfimg;
    }

    public void setLfimg(String lfimg) {
        this.lfimg = lfimg;
    }

    public String getVrkme() {
        return vrkme;
    }

    public void setVrkme(String vrkme) {
        this.vrkme = vrkme;
    }

    public String getMtart() {
        return mtart;
    }

    public void setMtart(String mtart) {
        this.mtart = mtart;
    }

    public String getMtbez() {
        return mtbez;
    }

    public void setMtbez(String mtbez) {
        this.mtbez = mtbez;
    }

    public String getWbstk() {
        return wbstk;
    }

    public void setWbstk(String wbstk) {
        this.wbstk = wbstk;
    }

    public String getPdstk() {
        return pdstk;
    }

    public void setPdstk(String pdstk) {
        this.pdstk = pdstk;
    }

    public String getWadatIst() {
        return wadatIst;
    }

    public void setWadatIst(String wadatIst) {
        this.wadatIst = wadatIst;
    }

    public String getStras() {
        return stras;
    }

    public void setStras(String stras) {
        this.stras = stras;
    }

    @Override
    public String toString() {
        return "ZsshipmentInTransit{" +
                "tknum='" + tknum + '\'' +
                ", kunnr='" + kunnr + '\'' +
                ", name1='" + name1 + '\'' +
                ", kunag='" + kunag + '\'' +
                ", name2='" + name2 + '\'' +
                ", ydhrq='" + ydhrq + '\'' +
                ", matnr='" + matnr + '\'' +
                ", maktx='" + maktx + '\'' +
                ", lfimg='" + lfimg + '\'' +
                ", vrkme='" + vrkme + '\'' +
                ", mtart='" + mtart + '\'' +
                ", mtbez='" + mtbez + '\'' +
                ", wbstk='" + wbstk + '\'' +
                ", pdstk='" + pdstk + '\'' +
                ", wadatIst='" + wadatIst + '\'' +
                ", stras='" + stras + '\'' +
                ", mvgr1='" + mvgr1 + '\'' +
                ", unit='" + unit + '\'' +
                '}';
    }
}
