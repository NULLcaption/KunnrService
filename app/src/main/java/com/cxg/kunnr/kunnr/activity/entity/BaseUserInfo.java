package com.cxg.kunnr.kunnr.activity.entity;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "userinfo")
public class BaseUserInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	@DatabaseField(id = true)
	private String userId;
	@DatabaseField
	private String password;
	@DatabaseField
	private String userName;
	@DatabaseField
	private String orgId;
	@DatabaseField
	private String orgName;
	@DatabaseField
	private String mobile;
	@DatabaseField
	private String roleId;
	@DatabaseField
	private String status;
	@DatabaseField
	private String roleType;
	@DatabaseField
	private String userCode;
	@DatabaseField
	private String couldId;
	@DatabaseField
	private String localpassword;
	@DatabaseField
	private String remberpsd;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRoleType() {
		return roleType;
	}
	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	public String getCouldId() {
		return couldId;
	}
	public void setCouldId(String couldId) {
		this.couldId = couldId;
	}
	public String getLocalpassword() {
		return localpassword;
	}
	public void setLocalpassword(String localpassword) {
		this.localpassword = localpassword;
	}
	public String getRemberpsd() {
		return remberpsd;
	}
	public void setRemberpsd(String remberpsd) {
		this.remberpsd = remberpsd;
	}
	@Override
	public String toString() {
		return "BaseUserInfo [userId=" + userId + ", password=" + password
				+ ", userName=" + userName + ", orgId=" + orgId + ", orgName="
				+ orgName + ", mobile=" + mobile + ", roleId=" + roleId
				+ ", status=" + status + ", roleType=" + roleType
				+ ", userCode=" + userCode + ", couldId=" + couldId
				+ ", localpassword=" + localpassword + ", remberpsd="
				+ remberpsd + "]";
	}
	

}
