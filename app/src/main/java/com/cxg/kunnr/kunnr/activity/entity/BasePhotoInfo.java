package com.cxg.kunnr.kunnr.activity.entity;


import java.io.Serializable;

import com.cxg.kunnr.kunnr.activity.application.XPPApplication.Status;
import com.cxg.kunnr.kunnr.activity.application.XPPApplication.PhotoType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "photoInfo")
public class BasePhotoInfo implements Serializable{

	private static final long serialVersionUID = 1L;
	@DatabaseField(generatedId = true)
	protected int id;
	@DatabaseField
	private String photoName;//照片手机里的名字
	@DatabaseField
	private Status status;//照片状态
	@DatabaseField
	private String dayType;//缓存的日期
	@DatabaseField
	protected PhotoType ptype;//照片类型
	@DatabaseField
	protected String photoPath;//照片路径 手机路径
	@DatabaseField
	protected String pztime;//拍照时间
	@DatabaseField
	protected String emplid;//员工编号
	@DatabaseField
	protected String custid;//客户编号
	@DatabaseField
	protected String routeid;//路线编号
	@DatabaseField
	protected String actid;//活动编号
	@DatabaseField
	protected String timestamp;//时间戳
	@DatabaseField
	protected String seq;//照片序号（暂无）
	@DatabaseField
	protected long pkId;//外键ID
	@DatabaseField
	protected String custName;//客户名
	@DatabaseField
	protected String updateTime;//上传时间
	@DatabaseField
	protected String MIAItemId;//
	@DatabaseField
	protected String MIADetailId;
	protected String phototype;//照片类型中文
	protected String photoaddress;//拍照地址
	public String getPhotoaddress() {
		return photoaddress;
	}
	public void setPhotoaddress(String photoaddress) {
		this.photoaddress = photoaddress;
	}
	public String getPhototype() {
		return phototype;
	}
	public void setPhototype(String phototype) {
		this.phototype = phototype;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPhotoName() {
		return photoName;
	}
	public void setPhotoName(String photoName) {
		this.photoName = photoName;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public PhotoType getPtype() {
		return ptype;
	}
	public void setPtype(PhotoType ptype) {
		this.ptype = ptype;
	}
	public String getPhotoPath() {
		return photoPath;
	}
	public void setPhotoPath(String photoPath) {
		this.photoPath = photoPath;
	}
	public String getPztime() {
		return pztime;
	}
	public void setPztime(String pztime) {
		this.pztime = pztime;
	}
	public String getEmplid() {
		return emplid;
	}
	public void setEmplid(String emplid) {
		this.emplid = emplid;
	}
	public String getCustid() {
		return custid;
	}
	public void setCustid(String custid) {
		this.custid = custid;
	}
	public String getRouteid() {
		return routeid;
	}
	public void setRouteid(String routeid) {
		this.routeid = routeid;
	}
	public String getActid() {
		return actid;
	}
	public void setActid(String actid) {
		this.actid = actid;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getSeq() {
		return seq;
	}
	public void setSeq(String seq) {
		this.seq = seq;
	}
	public String getDayType() {
		return dayType;
	}
	public void setDayType(String dayType) {
		this.dayType = dayType;
	}
	public long getPkId() {
		return pkId;
	}
	public void setPkId(long pkId) {
		this.pkId = pkId;
	}
	public String getCustName() {
		return custName;
	}
	public void setCustName(String custName) {
		this.custName = custName;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public String getMIAItemId() {
		return MIAItemId;
	}
	public void setMIAItemId(String mIAItemId) {
		MIAItemId = mIAItemId;
	}
	public String getMIADetailId() {
		return MIADetailId;
	}
	public void setMIADetailId(String mIADetailId) {
		MIADetailId = mIADetailId;
	}
	
	
}
