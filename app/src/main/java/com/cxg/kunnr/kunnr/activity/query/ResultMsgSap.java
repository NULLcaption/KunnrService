package com.cxg.kunnr.kunnr.activity.query;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Description: Sap返回值实体类
 * author: xg.chen
 * time: 2017/11/29
 * version: 1.0
 */
@DatabaseTable(tableName = "ResultMsgSap")
public class ResultMsgSap  implements Serializable {

    @DatabaseField
    private String EvCode;//返回码：数值0代表成功；数值1代表不成功
    @DatabaseField
    private String EvMsg;//返回信息

    public String getEvCode() {
        return EvCode;
    }

    public void setEvCode(String evCode) {
        EvCode = evCode;
    }

    public String getEvMsg() {
        return EvMsg;
    }

    public void setEvMsg(String evMsg) {
        EvMsg = evMsg;
    }
}
