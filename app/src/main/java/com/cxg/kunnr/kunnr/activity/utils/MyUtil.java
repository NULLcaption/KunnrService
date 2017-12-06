package com.cxg.kunnr.kunnr.activity.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.cxg.kunnr.kunnr.activity.provider.DataProviderFactory;
import com.cxg.kunnr.kunnr.activity.query.PhotoInfo;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Description: 工具类
 * author: xg.chen
 * time: 2017/11/27
 * version: 1.0
 */

public class MyUtil {

    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");

    public boolean compareDate(String str1, String str2) {
        if (str2 == null || "null".equals(str2) || "".equals(str2)) {
            return false;
        }
        return str1.equals(str2);
    }

    public String getWeekDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int i = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (i <= 0) {
            i = 7;
        }
        return String.valueOf(i);
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager mgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] info = mgr.getAllNetworkInfo();
        if (info != null) {
            for (int i = 0; i < info.length; i++) {
                if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Description:创建图片存储的位置
     * author: xg.chen
     * time: 2017/11/27
     * version: 1.0
     */
    public static Map<String, Bitmap> buildThum(List<PhotoInfo> photoList, int width , int height) {
        Map<String, Bitmap> maps = new TreeMap<>();
        List<String> paths = new ArrayList<>();
        for (int i = 0; i < photoList.size(); i++) {
            paths.add(DataProviderFactory.getDirName + photoList.get(i).getPhotoName() + ".jpg");
        }
        if (!paths.isEmpty()) {
            for (String path : paths) {
                Bitmap bp =PictureShowUtils.getImageBitByPath(path,width, height);
                if(bp!=null){
                    maps.put(path, bp);
                }
            }
        }
        return maps;
    }

    @SuppressLint("SimpleDateFormat")
    public static String getTime(String time) throws Exception{
        if(time==null||"".equals(time)){
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date date =  sdf.parse(time);
        SimpleDateFormat hh =new SimpleDateFormat("HH:mm:ss");
        String str = hh.format(date) ;
        return str;
    }

    @SuppressLint("SimpleDateFormat")
    public static String getDate(String time) throws Exception{
        java.util.Date date =  f.parse(time);
        SimpleDateFormat hh =new SimpleDateFormat("yyyyMMdd");
        String str = hh.format(date)+"000000" ;
        return str;
    }



    public static String addZeroForNum(String str, int strLength) {
        int strLen = str.length();
        if (strLen < strLength) {
            while (strLen < strLength) {
                StringBuffer sb = new StringBuffer();
                sb.append("0").append(str);
                str = sb.toString();
                strLen = str.length();
            }
        }
        return str;
    }

}
