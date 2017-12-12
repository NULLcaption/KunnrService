package com.cxg.kunnr.kunnr.activity.provider;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.cxg.kunnr.kunnr.activity.application.XPPApplication;
import com.cxg.kunnr.kunnr.activity.entity.BaseDictionary;
import com.cxg.kunnr.kunnr.activity.query.KunnrPodDetail;
import com.cxg.kunnr.kunnr.activity.query.KunnrPodTotal;
import com.cxg.kunnr.kunnr.activity.query.PhotoInfo;
import com.cxg.kunnr.kunnr.activity.query.ResultMsgSap;
import com.cxg.kunnr.kunnr.activity.query.UserInfo;
import com.cxg.kunnr.kunnr.activity.query.Zskunnr;
import com.cxg.kunnr.kunnr.activity.query.ZsshipmentInTransit;
import com.cxg.kunnr.kunnr.activity.query.ZsshipmentInTransitH;
import com.cxg.kunnr.kunnr.activity.utils.EncryptUtil;
import com.cxg.kunnr.kunnr.activity.utils.Helpers;
import com.cxg.kunnr.kunnr.activity.utils.HttpUtil;
import com.cxg.kunnr.kunnr.activity.utils.PhotoUtil;
import com.cxg.kunnr.kunnr.activity.utils.ReadPhoneStateUtil;
import com.cxg.kunnr.kunnr.activity.utils.ResultMessage;
import com.cxg.kunnr.kunnr.activity.utils.UploadUtil;
import com.cxg.kunnr.kunnr.activity.utils.WebserviceUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.lang.StringUtils;

/**
 * Description: 通过webservice获取数据之接口
 * author: xg.chen
 * time: 2017/11/20
 * version: 1.0
 */
public class WebService implements IDataProvider {
    private static WebService instance;
    private static final String TAG = "WebService";
    /** app服务地址(常用测试地址：本地，测试机，正式机)*/
    private static String OPENAPIURL = "http://exp.zjxpp.com:8186";//apn 生产机
// 	private static String OPENAPIURL = "http://exptest.zjxpp.com:7186";//测试机地址
//    private static String OPENAPIURL = "http://10.3.3.47:7186";//apn 测试机
    /** 图片下载服务器地址 */
//    public static final String DOWNPHOTO = "http://exptest.zjxpp.com:7186/upload_file/crm/slave/photo/";
    public static final String DOWNPHOTO = "http://exp.zjxpp.com:8186/upload_file/crm/slave/photo/";

    /** app服务接口*/
    private static final String GET_PERFORMANCE_URL = "/mobilePlatform/router/login";
    private static final String LOGIN = "/login";
    private static final String UPLOADLOGINLOG = "/uploadLoginLog";
    private static final String VERSION = "/version";
    private static final String PHOTOUPLOADPOD = "/photoPod";
    private static final String WARRING = "/warring";
    private static final String TIME = "/time";
    private static final String PODTOATL = "/podTotal";
    private static final String PODDETAIL = "/podDetail";
    private static final String PHOTOSTATUS = "/photoStatus";
    private static final String UPDATEPODTOATL = "/updatePodTotal";
    private static final String UPDATEPODTOATLS = "/updatePodTotals";
    private static final String PODFILE = "/podFile";
    private static final String PODLIST = "/podInfo";
    private static final String PODDETAILLIST = "/podDetailInfo";
    private static final String PODOK = "/podOk";

    private WebService() {
        super();
    }

    public static IDataProvider getInstance() {
        if (instance == null)
            instance = new WebService();
        return instance;
    }

    @Override
    public void startDataUpdateTasks(Activity activity) {
        Context ctx = DataProviderFactory.getContext();
        if (ctx != null) {
            SharedPreferences sp = ctx.getSharedPreferences("XPPWebService",
                    Context.MODE_PRIVATE);

            if (sp.contains("lastUpdate")) {
                Date now = new Date();
                String str1 = sp.getString("lastUpdate", "");
                String str2 = Helpers.getDateStrWithoutTime(now);
                if (str1.startsWith(str2)) {
                    Log.d(TAG, "No updates needed at this time.");
                    return;
                }
            }

            if (UpdateTask.getInstance().getStatus() != AsyncTask.Status.RUNNING) {
                sp.edit().putInt("lastUpdatedShopSequence", -1).commit();
                new UpdateTask(activity, false).execute();
            }
        }

    }

    /**
     * Description: 获取服务器时间
     * author: xg.chen
     * time: 2017/11/27
     * version: 1.0
     */
    @Override
    public String getTime() {
        try {
            String result = HttpUtil.post(OPENAPIURL + GET_PERFORMANCE_URL
                    + TIME, null);
            if (result != null) {
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Description: 获取图片路径
     * author: xg.chen
     * time: 2017/11/28
     * version: 1.0
     */
    @Override
    public List<String> loadingPhotoURL() {
        List<String> images = new ArrayList<>();
        String ip = DOWNPHOTO;
        for (int i = 0; i < 2; i++) {
            int name = i;
            String url = ip + name + ".jpg";
            images.add(url);
        }
        return images;
    }

    /**
     * Description: 登录信息提交
     * author: xg.chen
     * time: 2017/11/20
     * version: 1.0
     */
    public int login(String password) {
        Map<String, String> params = new HashMap<>();
        String chkpsw = DataProviderFactory.getRemberpsd();
        params.put("mobile", DataProviderFactory.getLoginName());
        params.put("password", password);
        try {
            //post request for open API url
            String result = HttpUtil.post(OPENAPIURL + GET_PERFORMANCE_URL
                    + LOGIN, params);
            //for result judgement
            if (result != null) {
                if (XPPApplication.UPLOAD_FAIL_CONNECT_SERVER.equals(result)) {// 连接超时
                    UserInfo info1 = UserInfo
                            .findByLoginName(DataProviderFactory.getLoginName());
                    if (info1 != null) {// 离线登陆
                        if (info1.getPassword().equals(
                                EncryptUtil.md5Encry(password))) {
                            return XPPApplication.OFFLINE_LOADED;
                        }
                    } else {
                        return XPPApplication.FAIL_CONNECT_SERVER;
                    }
                }
                Gson gson = new Gson();
                UserInfo userInfo = gson.fromJson(result, UserInfo.class);
                if (Integer.valueOf(userInfo.getStatus()) == XPPApplication.SUCCESS) {
                    if ("Y".equals(chkpsw)) {
                        userInfo.setRemberpsd(chkpsw);
                        userInfo.setLocalpassword(password);
                        DataProviderFactory.setLocalPassword(password);
                        DataProviderFactory.setRemberpsd(chkpsw);
                    }
                    userInfo.save();
                    DataProviderFactory.setUserId(userInfo.getUserId());
                    DataProviderFactory.setRoleId(userInfo.getRoleId());
                    DataProviderFactory.setOrgId(userInfo.getOrgId());
                    DataProviderFactory.setCouldId(userInfo.getCouldId());
                    uploadLoginLog("online");
                } else if (Integer.valueOf(userInfo.getStatus()) == XPPApplication.ERR_PASSWORD) {
                    return XPPApplication.ERR_PASSWORD;
                } else if (Integer.valueOf(userInfo.getStatus()) == XPPApplication.NO_USER) {
                    return XPPApplication.NO_USER;
                } else if (Integer.valueOf(userInfo.getStatus()) == XPPApplication.ERR_ROLE) {
                    return XPPApplication.ERR_ROLE;
                } else if (Integer.valueOf(userInfo.getStatus()) == XPPApplication.NO_MOBILE) {
                    return XPPApplication.NO_MOBILE;
                } else if (Integer.valueOf(userInfo.getStatus()) == XPPApplication.NOTBUSINESSPHONE) {
                    return XPPApplication.NOTBUSINESSPHONE;
                } else {
                    UserInfo info = UserInfo
                            .findByLoginName(DataProviderFactory.getLoginName());
                    if (info != null) {
                        if (info.getPassword().equals(
                                EncryptUtil.md5Encry(password))) {
                            return XPPApplication.OFFLINE_LOADED;
                        }
                        return XPPApplication.OFFLINE_ERROR_PASSWORD;
                    }
                    return Integer.valueOf(userInfo.getStatus());
                }
            } else {// 网络不通
                return XPPApplication.NO_NETWORK;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return XPPApplication.FAIL;
        }
        // 验证版本
        /*BaseDictionary bd = DataProviderFactory.getProvider().getVersion();
		if (bd != null) {
			if (!bd.getItemName().equals(
					XPPApplication.getVersionName(DataProviderFactory
							.getContext()))) {
				SharedPreferences settings = DataProviderFactory
						.getContext()
						.getSharedPreferences("PrefsFile", Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = settings.edit();
				editor.putString("version", bd.getDictTypeValue());
				editor.commit();
				return XPPApplication.UPDATE_VERSION;
			}
		}*/
        return XPPApplication.SUCCESS;
    }

    /**
     * Description: 登录信息
     * author: xg.chen
     * time: 2017/11/20
     * version: 1.0
     */
    @Override
    public boolean uploadLoginLog(String status) {
        ReadPhoneStateUtil ps = new ReadPhoneStateUtil(
                DataProviderFactory.getContext());
        Map<String, String> params = new HashMap<>();
        if ("online".equals(status)) {// 登陆
            params.put("userId", DataProviderFactory.getUserId());
            params.put("imei", ps.getIMEI());
            params.put("imsi", ps.getIMSI());
            params.put("loginMobile", ps.getPhoneNumber());
            params.put("handSetInfo", ps.getHandSetInfo());
        } else {// 登出
            params.put("loginLogId", DataProviderFactory.getLoginLogId());
        }
        params.put("status", status);
        params.put("packageName", "superviseMoblie");
        params.put("version",
                XPPApplication.getVersionName(DataProviderFactory.getContext()));
        Gson gs = new Gson();
        try {
            String result = HttpUtil.post(OPENAPIURL + GET_PERFORMANCE_URL
                    + UPLOADLOGINLOG, params);
            if (result == null
                    || XPPApplication.UPLOAD_FAIL_CONNECT_SERVER.equals(result)) {
                return false;
            } else {
                ResultMessage s = gs.fromJson(result, ResultMessage.class);
                if (XPPApplication.UPLOAD_SUCCESS.equals(s.getResultCode())
                        && "online".equals(status)) {
                    DataProviderFactory.setLoginLogId(s.getResultDesc());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Description: 获取版本信息
     * author: xg.chen
     * time: 2017/11/20
     * version: 1.0
     */
    public BaseDictionary getVersion() {
        BaseDictionary bd = new BaseDictionary();
        try {
            String result = HttpUtil.post(OPENAPIURL + GET_PERFORMANCE_URL
                    + VERSION, null);
            if (result == null
                    || XPPApplication.UPLOAD_FAIL_CONNECT_SERVER.equals(result)) {
                return bd;
            }
            Gson gson = new Gson();
            List<BaseDictionary> list = gson.fromJson(result,
                    new TypeToken<List<BaseDictionary>>() {
                    }.getType());
            if (list == null || list.size() == 0) {
                return null;
            }
            bd = list.get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return bd;
    }

    /**
     * Description: 上传图片
     * author: xg.chen
     * time: 2017/11/20
     * version: 1.0
     */
    @Override
    public String uploadPicture(PhotoInfo photoInfo) {
        try {
            String s = UploadUtil.getPhotoParams(DataProviderFactory.getDirName
                    + photoInfo.getPhotoName() + ".jpg");
            if (s == null) {
                return "false";
            }
            Map<String, String> params = new HashMap<>();
            params.put("photo", s);
            params.put("photoType", photoInfo.getPtype().toString());
            params.put("custId", photoInfo.getCustid());//运单号
            params.put("userId", photoInfo.getEmplid());
            params.put("dayType", photoInfo.getDayType());
            params.put("activityId", photoInfo.getMIADetailId());
            params.put("itemId", photoInfo.getMIAItemId());
            params.put("pzTime", PhotoUtil.getpicTime(photoInfo.getPhotoName()));
            params.put("seq",photoInfo.getSeq());

            System.out.println("+++>>>>>"+params);
            String result = HttpUtil.post(OPENAPIURL + GET_PERFORMANCE_URL
                    + PHOTOUPLOADPOD, params);
            if (result != null) {
                if (XPPApplication.UPLOAD_SUCCESS.equals(result)) {
                    photoInfo.setStatus(XPPApplication.Status.FINISHED);
                    photoInfo.update();

                    return "true";
                } else {
                    return "false";
                }
            }
            return "false";

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "false";
    }

    /**
     * Description: 获取在途运单汇总列表
     * author: xg.chen
     * time: 2017/11/22
     * version: 1.0
     */
    @Override
    public List<ZsshipmentInTransitH> getPodInfoTask(String kunnrId) {
        List<ZsshipmentInTransitH> zsshipmentInTransitHList = new ArrayList<>();
        try {
            //参数
            Map<String, String> params = new HashMap<>();
            params.put("kunnr",kunnrId);
            //请求路径
            String result = HttpUtil.post(OPENAPIURL + GET_PERFORMANCE_URL
                    + PODLIST, params);
            //返回参数
            if (result != null || XPPApplication.UPLOAD_FAIL_CONNECT_SERVER.equals(result)) {
                Gson gson = new Gson();
                zsshipmentInTransitHList = gson.fromJson(result,
                        new TypeToken<List<ZsshipmentInTransitH>>() {
                        }.getType());
            }

            return zsshipmentInTransitHList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Description: 异步在EXP中创建获取到的在途运单数据
     * author: xg.chen
     * time: 2017/11/28
     * version: 1.0
     */
    @Override
    public String creatKunnrPodTotalTask(KunnrPodTotal kunnrPodTotal) {
        String result = "";
        try{
            Map<String, String> params = new HashMap<>();
            params.put("waybillId",kunnrPodTotal.getWaybillId());
            params.put("kunnr",kunnrPodTotal.getKunnr());
            params.put("estimateDate",kunnrPodTotal.getEstimateDate());
            params.put("creator",kunnrPodTotal.getCreator());
            params.put("flag",kunnrPodTotal.getFlag());
            params.put("type",kunnrPodTotal.getType());

            System.out.println("+++>>>>>"+params);
            result = HttpUtil.post(OPENAPIURL + GET_PERFORMANCE_URL
                    + PODTOATL, params);
            if (StringUtils.isNotEmpty(result)) {
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Description: 异步请求EXP中更新存储的该订单
     * author: xg.chen
     * time: 2017/11/29
     * version: 1.0
     */
    @Override
    public String updateKunnrPodTotalTask(KunnrPodTotal kunnrPodTotal) {
        String result = "";
        try{
            Map<String, String> params = new HashMap<>();
            params.put("waybillId",kunnrPodTotal.getWaybillId());
            params.put("kunnr",kunnrPodTotal.getKunnr());
            params.put("flag",kunnrPodTotal.getFlag());
            params.put("abnormal",kunnrPodTotal.getAbnormal());
            params.put("arrivalDate",kunnrPodTotal.getArrivalDate());
            params.put("remark",kunnrPodTotal.getRemark());
            params.put("creator",DataProviderFactory.getUserId());
            params.put("modifier",DataProviderFactory.getUserId());
            params.put("status",kunnrPodTotal.getStatus());
            params.put("address",kunnrPodTotal.getAddress());
            params.put("longitude",kunnrPodTotal.getLongitude());
            params.put("latitude",kunnrPodTotal.getLatitude());

            System.out.println("+++>>>>>"+params);
            result = HttpUtil.post(OPENAPIURL + GET_PERFORMANCE_URL
                    + UPDATEPODTOATL, params);
            if (StringUtils.isNotEmpty(result)) {
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Description:异步请求EXP中更新该运单状态
     * author: xg.chen
     * time: 2017/11/30
     * version: 1.0
     */
    @Override
    public String updateKunnrPodTotalStatusTask(KunnrPodTotal kunnrPodTotal) {
        String result = "";
        try{
            Map<String, String> params = new HashMap<>();
            params.put("waybillId",kunnrPodTotal.getWaybillId());
            params.put("kunnr",kunnrPodTotal.getKunnr());
            params.put("flag",kunnrPodTotal.getFlag());
            params.put("creator",kunnrPodTotal.getCreator());

            System.out.println("+++>>>>>"+params);
            result = HttpUtil.post(OPENAPIURL + GET_PERFORMANCE_URL
                    + UPDATEPODTOATLS, params);
            if (StringUtils.isNotEmpty(result)) {
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Description: 异步创建pod照片存放列表
     * author: xg.chen
     * time: 2017/11/29
     * version: 1.0
     */
    @Override
    public String creatKunnrPodFileTask(String param) {
        String result = "";
        try{
            Map<String, String> params = new HashMap<>();
            params.put("waybillId",param);

            System.out.println("+++>>>>>"+params);
            result = HttpUtil.post(OPENAPIURL + GET_PERFORMANCE_URL
                    + PODFILE, params);
            if (StringUtils.isNotEmpty(result)) {
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Description: 异步在EXP中创建获取到的在途运单明细数据
     * author: xg.chen
     * time: 2017/11/28
     * version: 1.0
     */
    @Override
    public String creatKunnrPodDetailTask(KunnrPodDetail kunnrPodDetail) {
        String result = "";
        try{
            Map<String, String> params = new HashMap<>();
            params.put("waybillId",kunnrPodDetail.getWaybillId());
            params.put("mvgr1",kunnrPodDetail.getMvgr1());
            params.put("lfimg",kunnrPodDetail.getLfimg());
            params.put("unit",kunnrPodDetail.getUnit());
            params.put("count",kunnrPodDetail.getCount());

            System.out.println("+++>>>>>"+params);
            result = HttpUtil.post(OPENAPIURL + GET_PERFORMANCE_URL
                    + PODDETAIL, params);
            if (StringUtils.isNotEmpty(result)) {
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Description: 根据运单号获取运单明细列表
     * author: xg.chen
     * time: 2017/11/23
     * version: 1.0
     */
    @Override
    public List<ZsshipmentInTransit> getPodDetailInfoTask(String tknum) {
        List<ZsshipmentInTransit> zsshipmentInTransitList = new ArrayList<>();
        try {
            //参数
            Map<String, String> params = new HashMap<>();
            params.put("waybillId",tknum);
            //请求路径
            String result = HttpUtil.post(OPENAPIURL + GET_PERFORMANCE_URL
                    + PODDETAILLIST, params);
            //返回参数
            if (result != null || XPPApplication.UPLOAD_FAIL_CONNECT_SERVER.equals(result)) {
                Gson gson = new Gson();
                zsshipmentInTransitList = gson.fromJson(result,
                        new TypeToken<List<ZsshipmentInTransit>>() {
                        }.getType());
            }
            return zsshipmentInTransitList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Description: 获取是否为拍照的特殊时期
     * author: xg.chen
     * time: 2017/11/29
     * version: 1.0
     */
    @Override
    public String getPhotoStatus() {
        try {
            String result = HttpUtil.post(OPENAPIURL + GET_PERFORMANCE_URL
                    + PHOTOSTATUS, null);
            if (result != null) {
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Description: 在SAP中做POD确认到货
     * author: xg.chen
     * time: 2017/11/29
     * version: 1.0
     */
    @Override
    public ResultMsgSap kunnrPodConfirmSapTask(KunnrPodTotal kunnrPodTotal) {
        ResultMsgSap resultMsgSap = new ResultMsgSap();
        try {
            //参数
            String tknum = kunnrPodTotal.getWaybillId();
            String podDate = kunnrPodTotal.getArrivalDate();

            Map<String, String> params = new HashMap<>();
            params.put("waybillId",tknum);
            params.put("podDate",podDate);

            //请求路径
            String result = HttpUtil.post(OPENAPIURL + GET_PERFORMANCE_URL
                    + PODOK, params);
            //返回参数
            if (StringUtils.isNotEmpty(result)) {
                if (result.equals("0")) {
                    resultMsgSap.setEvCode(result);
                    resultMsgSap.setEvMsg("创建成功");
                } else {
                    resultMsgSap.setEvCode(result);
                    resultMsgSap.setEvMsg("创建失败");
                }
            }
            return resultMsgSap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Description: EXP字典中获取异常信息
     * author: xg.chen
     * time: 2017/11/23
     * version: 1.0
     */
    public List<BaseDictionary> getWarring() {
        List<BaseDictionary> list = new ArrayList<>();
        try {
            String result = HttpUtil.post(OPENAPIURL + GET_PERFORMANCE_URL
                    + WARRING, null);
            if (result == null
                    || XPPApplication.UPLOAD_FAIL_CONNECT_SERVER.equals(result)) {
                return list;
            }
            Gson gson = new Gson();
            list = gson.fromJson(result,
                    new TypeToken<List<BaseDictionary>>() {
                    }.getType());
            if (list == null || list.size() == 0) {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

}
