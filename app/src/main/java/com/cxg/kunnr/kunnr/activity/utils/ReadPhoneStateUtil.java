package com.cxg.kunnr.kunnr.activity.utils;

import android.content.Context;
import android.telephony.TelephonyManager;
/**   
 * @Title: ReadPhoneStateUtil.java 
 * @Package com.xpp.moblie.util 
 */

public class ReadPhoneStateUtil {
	private  TelephonyManager mTelephonyMgr ;
	public ReadPhoneStateUtil (Context context){
		 mTelephonyMgr = (TelephonyManager)context. getSystemService(Context.TELEPHONY_SERVICE);
	}
	
	public String  getIMSI(){
		try {
			return mTelephonyMgr.getSimSerialNumber();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public String  getIMEI(){
		try {
			return mTelephonyMgr.getDeviceId();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public String  getPhoneNumber(){
		try {
			return mTelephonyMgr.getLine1Number();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public  String getHandSetInfo(){
		try {
			String handSetInfo=
					"手机型号:" + android.os.Build.MODEL +
							",SDK版本:" + android.os.Build.VERSION.SDK +
							",系统版本:" + android.os.Build.VERSION.RELEASE;
			return handSetInfo;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";

	}

}
