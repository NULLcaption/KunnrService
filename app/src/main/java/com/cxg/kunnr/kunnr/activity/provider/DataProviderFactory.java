package com.cxg.kunnr.kunnr.activity.provider;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

/**
 * Description: 记录数据接口存储调用
 * author: xg.chen
 * time: 2017/11/20
 * version: 1.0
 */
public class DataProviderFactory {

	public static final String PREFS_NAME = "PrefsFile";
	public static final String PREFS_LOGINNAME = "loginName";
	public static final String PREFS_USERID = "userId";
	public static final String PREFS_PASSWORD = "password";
	public static final String PREFS_DAYTYPE = "dayType";
	public static final String PREFS_MENU = "menu";
	public static final String PREFS_ROLEID = "roleId";
	public static final String PREFS_ORGID = "orgId";
	public static final String PREFS_COULDID = "couldId";
	public static final String PREFS_STATUS = "status";
	public static final String PREFS_DEFAULT_KUNNR_NAME = "default_kunnr_name";
	public static final String PREFS_DEFAULT_KUNNR = "default_kunnr";
	public static final String PREFS_DIVINAME = "diviName";
	public static final String PREFS_ZW104 = "zwl04";
	public static final String PREFS_LOGINLOGID = "loginLogId";
	public static final String USER_TYPE = "userType";
	public static final String PREFS_PHONESELFNUM = "phoneSelfNum";
	public static final String PREFS_LOCALPASSWORD = "localpassword";
	public static final String PREFS_REMBERPSD = "remberpsd";
	public static final String PREFS_DEFAULT_KUNNR_NAME1 = "default_kunnr_name1";
	public static final String PREFS_DEFAULT_KUNNR1 = "default_kunnr1";
	public static final String PREFS_DIVINAME1 = "diviName1";
	public static final String PREFS_ZW1041 = "zwl041";
	public static final String PREFS_CUSTFXDATE = "custfxdate";
	public static final String PREFS_WXRETURN = "wxreturn";
	public static final String PREFS_CUSTID = "custId";
	public static final String getDirName = Environment
			.getExternalStorageDirectory().toString() + "/xpp/Photo/";
	public static Context ctx;

	public static Context getContext() {
		return ctx;
	}

	public static void setContext(Context ctx) {
		DataProviderFactory.ctx = ctx;
	}

	public static IDataProvider getProvider() {
		return WebService.getInstance();
	}

	public static IDataProvider getProvider(Context ctx) {
		DataProviderFactory.ctx = ctx;
		return getProvider();
	}

	// loginName
	public static String getLoginName() {
		SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME,
				Context.MODE_PRIVATE);
		String userName = settings.getString(PREFS_LOGINNAME, "");// 18657276623
		return userName;
	}

	public static void setLoginName(String loginName) {
		SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(DataProviderFactory.PREFS_LOGINNAME, loginName);
		editor.commit();
	}

	//获取字典中是否为特殊时期
	public static String getPhotoStatus() {
		SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME,
				Context.MODE_PRIVATE);
		String userName = settings.getString(PREFS_STATUS, "");// 18657276623
		return userName;
	}

	public static void setPhotoStatus(String status) {
		SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(DataProviderFactory.PREFS_STATUS, status);
		editor.commit();
	}

	// userType
	public static String getUserType() {
		SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME,
				Context.MODE_PRIVATE);
		String userType = settings.getString(USER_TYPE, "TYPE_NULL");
		return userType;
	}

	public static void setUserType(String userType) {
		SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(DataProviderFactory.USER_TYPE, userType);
		editor.commit();
	}

	// userid
	public static String getUserId() {
		SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME,
				Context.MODE_PRIVATE);
		String userName = settings.getString(PREFS_USERID, "");
		return userName;
	}

	public static void setUserId(String userId) {
		SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(DataProviderFactory.PREFS_USERID, userId);
		editor.commit();
	}

	// PREFS_PHONESELFNUM = "phoneSelfNum"
	public static String getPhoneSelfNum() {
		SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME,
				Context.MODE_PRIVATE);
		String userName = settings.getString(PREFS_PHONESELFNUM, "");
		return userName;
	}

	public static void setPhoneSelfNum(String phoneSelfNum) {
		SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(DataProviderFactory.PREFS_PHONESELFNUM, phoneSelfNum);
		editor.commit();
	}

	// password
	public static String getPassword() {
		SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME,
				Context.MODE_PRIVATE);
		String userName = settings.getString(PREFS_PASSWORD, "");
		return userName;
	}

	public static void setPassword(String password) {
		SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(DataProviderFactory.PREFS_PASSWORD, password);
		editor.commit();
	}

	public static String getDayType() {
		SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME,
				Context.MODE_PRIVATE);
		String userName = settings.getString(PREFS_DAYTYPE, null);
		return userName;
	}

	public static void setDayType(String dayType) {
		SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(DataProviderFactory.PREFS_DAYTYPE, dayType);
		editor.commit();
	}

	public static int getMenuId() {
		SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME,
				Context.MODE_PRIVATE);
		int menuId = settings.getInt(PREFS_MENU, 0);
		return menuId;
	}

	public static void setMenuId(int menuId) {
		SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt(DataProviderFactory.PREFS_MENU, menuId);
		editor.commit();
	}

	// roleId
	public static String getRoleId() {
		SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME,
				Context.MODE_PRIVATE);
		String userName = settings.getString(PREFS_ROLEID, null);
		return userName;
	}

	public static void setRoleId(String roleId) {
		SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(DataProviderFactory.PREFS_ROLEID, roleId);
		editor.commit();
	}

	// orgId
	public static String getOrgId() {
		SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME,
				Context.MODE_PRIVATE);
		String orgId = settings.getString(PREFS_ORGID, null);
		return orgId;
	}

	public static void setOrgId(String orgId) {
		SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(DataProviderFactory.PREFS_ORGID, orgId);
		editor.commit();
	}

	// CouldId
	public static String getCouldId() {
		SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME,
				Context.MODE_PRIVATE);
		String couldId = settings.getString(PREFS_COULDID, null);
		return couldId;
	}

	public static void setCouldId(String couldId) {
		SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(DataProviderFactory.PREFS_COULDID, couldId);
		editor.commit();
	}

	// LoginId
	public static String getLoginLogId() {
		SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME,
				Context.MODE_PRIVATE);
		String editor = settings.getString(PREFS_LOGINLOGID, null);
		return editor;
	}

	public static void setLoginLogId(String loginId) {
		SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(DataProviderFactory.PREFS_LOGINLOGID, loginId);
		editor.commit();
	}

	// localpassword
	public static String getLocalPassword() {
		SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME,
				Context.MODE_PRIVATE);
		String local = settings.getString(PREFS_LOCALPASSWORD, "");
		return local;
	}

	public static void setLocalPassword(String localpassword) {
		SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(DataProviderFactory.PREFS_LOCALPASSWORD, localpassword);
		editor.commit();
	}

	public static String getRemberpsd() {
		SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME,
				Context.MODE_PRIVATE);
		String checkbox = settings.getString(PREFS_REMBERPSD, "");
		return checkbox;
	}

	public static void setRemberpsd(String remberpsd) {
		SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(DataProviderFactory.PREFS_REMBERPSD, remberpsd);
		editor.commit();
	}

	public static String getCustfxdate() {
		SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME,
				Context.MODE_PRIVATE);
		String checkbox = settings.getString(PREFS_CUSTFXDATE, "");
		return checkbox;
	}

	public static void setCustfxdate(String custfxdate) {
		SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(DataProviderFactory.PREFS_CUSTFXDATE, custfxdate);
		editor.commit();
	}

}