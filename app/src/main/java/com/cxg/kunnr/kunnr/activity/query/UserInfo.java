package com.cxg.kunnr.kunnr.activity.query;

import com.cxg.kunnr.kunnr.activity.entity.BaseUserInfo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserInfo extends BaseUserInfo {
	private static final long serialVersionUID = 1L;

	/**
	 * @Description:save user info 
	 * @author:xg.chen 
	 * @return
	 * @version:1.0
	 */
	public boolean save() {
		try {
			OrmHelper.getInstance().getUserInfoDao().createOrUpdate(this);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * @Description:find by login name for local sql
	 * @author:xg.chen 
	 * @param loginName
	 * @return
	 * @version:1.0
	 */
	public static UserInfo findByLoginName(String loginName) {
		try {
			List<UserInfo> list;
			if(loginName.matches("^[0-9]*$")&&loginName.length()==11){
				list = OrmHelper.getInstance().getUserInfoDao()
						.queryBuilder().where().eq("mobile", loginName).query();
			}else{
				list = OrmHelper.getInstance().getUserInfoDao()
						.queryBuilder().where().eq("userCode", loginName).query();
			}
			if (list != null && list.size() != 0) {
				return list.get(0);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static UserInfo findById(String userId) {
		try {
			List<UserInfo> list  = new ArrayList<UserInfo>();
			
				list = OrmHelper.getInstance().getUserInfoDao()
						.queryBuilder().where().eq("userId", userId).query();
		
			 
			if (list != null && list.size() != 0) {
				return list.get(0);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static List<UserInfo> findAll() {
		try {
			List<UserInfo> list = OrmHelper.getInstance().getUserInfoDao()
					.queryForAll();
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
