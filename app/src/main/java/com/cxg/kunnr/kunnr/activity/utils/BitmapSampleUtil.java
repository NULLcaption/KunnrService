package com.cxg.kunnr.kunnr.activity.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 图片地址工具类
 * author: xg.chen
 * time: 2017/11/27
 * version: 1.0
 */
public class BitmapSampleUtil {

	/** 图片下载服务器地址 */
	public static final String DOWNPHOTO = "http://exptest.zjxpp.com:7186/upload_file/crm/slave/photo/";

	/**
	 * Description: 图片的URL
	 * author: xg.chen
	 * time: 2017/11/27
	 * version: 1.0
	 */
	public static List<String> loadingPhotoURL () {
		List<String> images = new ArrayList<>();
		String ip = DOWNPHOTO;
		for (int i = 0; i < 1; i++) {
			int name = i;
			String url = ip + name + ".jpg";
			images.add(url);
		}
		return images;
	}

}
