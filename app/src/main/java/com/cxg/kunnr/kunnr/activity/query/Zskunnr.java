package com.cxg.kunnr.kunnr.activity.query;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.io.Serializable;
import java.util.Hashtable;

/**
 * Description: 经销商编号
 * author: xg.chen
 * time: 2017/11/22
 * version: 1.0
 */
@DatabaseTable(tableName = "Zskunnr")
public class Zskunnr implements Serializable,KvmSerializable {

    @DatabaseField
    private String Kunnr;

    public String getKunnr() {
        return Kunnr;
    }

    public void setKunnr(String kunnr) {
        Kunnr = kunnr;
    }

    @Override
    public String toString() {
        return "Zskunnr{" +
                "Kunnr='" + Kunnr + '\'' +
                '}';
    }

    @Override
    public Object getProperty(int i) {
        return null;
    }

    @Override
    public int getPropertyCount() {
        return 0;
    }

    @Override
    public void setProperty(int i, Object o) {

    }

    @Override
    public void getPropertyInfo(int i, Hashtable hashtable, PropertyInfo propertyInfo) {

    }
}
