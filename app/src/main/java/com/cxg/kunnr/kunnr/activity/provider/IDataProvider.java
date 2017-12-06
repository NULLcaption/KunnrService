package com.cxg.kunnr.kunnr.activity.provider;

import android.app.Activity;

import com.cxg.kunnr.kunnr.activity.entity.BaseDictionary;
import com.cxg.kunnr.kunnr.activity.query.KunnrPodDetail;
import com.cxg.kunnr.kunnr.activity.query.KunnrPodTotal;
import com.cxg.kunnr.kunnr.activity.query.PhotoInfo;
import com.cxg.kunnr.kunnr.activity.query.ResultMsgSap;
import com.cxg.kunnr.kunnr.activity.query.ZsshipmentInTransit;
import com.cxg.kunnr.kunnr.activity.query.ZsshipmentInTransitH;

import java.util.List;

/**
 * Description: app服务获取数据之公共接口
 * author: xg.chen
 * time: 2017/11/20
 * version: 1.0
 */

public interface IDataProvider {
    
    void startDataUpdateTasks(Activity activity);


    /** 登陆 **/
    int login(String password);

    /** 登陆信息记录 */
    boolean uploadLoginLog(String status);

    BaseDictionary getVersion();

    /** 照片上传接口 */
    String uploadPicture(PhotoInfo photoInfo);

    /*获取在途运单汇总列表*/
    List<ZsshipmentInTransitH> getPodInfoTask(String string);

    /*获取在途运单详细列表*/
    List<ZsshipmentInTransit> getPodDetailInfoTask(String string);

    /*获取异常信息*/
    List<BaseDictionary> getWarring();

    /*获取服务器时间*/
    String getTime();

    /*获取样例图片路径*/
    List<String> loadingPhotoURL();

    /*异步在EXP中创建获取到的在途运单数据*/
    String creatKunnrPodTotalTask(KunnrPodTotal kunnrPodTotal);

    /*异步在EXP中创建获取到的在途运单明细数据*/
    String creatKunnrPodDetailTask(KunnrPodDetail kunnrPodDetail);

    /*获取是否为拍照的特殊时期*/
    String getPhotoStatus();

    /*在SAP中做POD确认到货*/
    ResultMsgSap kunnrPodConfirmSapTask(KunnrPodTotal kunnrPodTotal);

    /*异步请求EXP中更新存储的该订单*/
    String updateKunnrPodTotalTask(KunnrPodTotal kunnrPodTotal);

    /*异步创建pod照片存放列表*/
    String creatKunnrPodFileTask(String param);

    /*异步请求EXP中更新存储的该订单状态*/
    String updateKunnrPodTotalStatusTask(KunnrPodTotal kunnrPodTotal);
}
