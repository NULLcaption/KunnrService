package com.cxg.kunnr.kunnr.activity.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
/**
 * Description: 文件上传工具类
 * author: xg.chen
 * time: 2017/11/30
 * version: 1.0
 */
public class UploadUtil {
	private static UploadUtil uploadUtil;
	private static final String BOUNDARY = UUID.randomUUID().toString(); // 边界标识
	// 随机生成
	private static final String PREFIX = "--";
	private static final String LINE_END = "\r\n";
	private static final String CONTENT_TYPE = "multipart/form-data"; // 内容类型

	private UploadUtil() {

	}

	/**
	 * 单例模式获取上传工具类
	 *
	 * @return
	 */
	public static UploadUtil getInstance() {
		if (null == uploadUtil) {
			uploadUtil = new UploadUtil();
		}
		return uploadUtil;
	}

	private static final String TAG = "UploadUtil";
	private int readTimeOut = 30 * 1000; // 读取超时
	private int connectTimeout = 30 * 1000; // 超时时间
	/***
	 * 请求使用多长时间
	 */
	private static int requestTime = 0;

	private static final String CHARSET = "utf-8"; // 设置编码

	/***
	 * 上传成功
	 */
	public static final int UPLOAD_SUCCESS_CODE = 1;
	/**
	 * 文件不存在
	 */
	public static final int UPLOAD_FILE_NOT_EXISTS_CODE = 2;
	/**
	 * 服务器出错
	 */
	public static final int UPLOAD_SERVER_ERROR_CODE = 3;
	/**
	 * 网络连接异常
	 */
	public static final int UPLOAD_NETERROR_ERROR_CODE = 4;

	protected static final int WHAT_TO_UPLOAD = 1;
	protected static final int WHAT_UPLOAD_DONE = 2;

	public String toUploadFile(String filepath, String fileKey,
							   String RequestURL, Map<String, String> param) {
		String result = null;
		long requestTime = System.currentTimeMillis();
		long responseTime = 0;

		HttpURLConnection conn = null;
		try {
			URL url2 = new URL(RequestURL);
			System.out.println(RequestURL);
			conn = (HttpURLConnection) url2.openConnection();

			conn.setReadTimeout(readTimeOut);
			conn.setConnectTimeout(connectTimeout);
			conn.setDoInput(true); // 允许输入流
			conn.setDoOutput(true); // 允许输出流
			conn.setUseCaches(false); // 不允许使用缓存
			conn.setRequestMethod("POST"); // 请求方式
			conn.setRequestProperty("Charset", CHARSET); // 设置编码
			conn.setRequestProperty("connection", "keep-alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary="
					+ BOUNDARY);

			/**
			 * 当文件不为空，把文件包装并且上传
			 */
			DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
			StringBuffer sb = null;
			String params = "";

			/***
			 * 以下是用于上传参数
			 */
			if (param != null && param.size() > 0) {
				Iterator<String> it = param.keySet().iterator();
				while (it.hasNext()) {
					sb = null;
					sb = new StringBuffer();
					String key = it.next();
					String value = param.get(key);
					sb.append(PREFIX).append(BOUNDARY).append(LINE_END);
					sb.append("Content-Disposition: form-data; name=\"")
							.append(key).append("\"").append(LINE_END)
							.append(LINE_END);
					sb.append(value).append(LINE_END);
					params = sb.toString();
					// Log.i(TAG, key + "=" + params + "##");
					dos.write(params.getBytes());
					// dos.flush();
				}
			}

			sb = null;
			params = null;
			sb = new StringBuffer();
			/**
			 * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
			 * filename是文件的名字，包含后缀名的 比如:abc.png
			 */
			sb.append(PREFIX).append(BOUNDARY).append(LINE_END);
			sb.append("Content-Disposition:form-data; name=\"" + fileKey
					+ "\"; filename=\"1.zip\"" + LINE_END);
			sb.append("Content-Type:application/x-zip-compressed" + LINE_END); // 这里配置的Content-type很重要的
			// ，用于服务器端辨别文件的类型的
			sb.append(LINE_END);
			params = sb.toString();
			sb = null;

			dos.write(params.getBytes());
			/** 上传文件 */
			InputStream is = new FileInputStream(new File(filepath));
			// onUploadProcessListener.initUpload((int)file.length());
			byte[] bytes = new byte[1024];
			int len = 0;
			// int curLen = 0;
			while ((len = is.read(bytes)) != -1) {
				// curLen += len;
				dos.write(bytes, 0, len);
				// onUploadProcessListener.onUploadProcess(curLen);
			}
			is.close();

			dos.write(LINE_END.getBytes());
			byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END)
					.getBytes();
			dos.write(end_data);
			dos.flush();

			/**
			 * 获取响应码 200=成功 当响应成功，获取响应的流
			 */
			int res = conn.getResponseCode();
			responseTime = System.currentTimeMillis();
			this.requestTime = (int) ((responseTime - requestTime) / 1000);
			Log.e(TAG, "response code:" + res);
			if (res == 200) {
				Log.e(TAG, "request success");
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(conn.getInputStream()));
				String line = reader.readLine();
				return line;
			} else {
				Log.e(TAG, "request error" + conn.getResponseMessage());
				sendMessage(UPLOAD_SERVER_ERROR_CODE, "上传失败：code=" + res);
				return "";
			}
		} catch (MalformedURLException e) {
			sendMessage(UPLOAD_SERVER_ERROR_CODE,
					"上传失败：error=" + e.getMessage());
			e.printStackTrace();
			return "errorFlie";
		} catch (IOException e) {
			sendMessage(UPLOAD_SERVER_ERROR_CODE,
					"上传失败：error=" + e.getMessage());
			e.printStackTrace();
			return "errorFlie";
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
	}

	/**
	 * 发送上传结果
	 *
	 * @param responseCode
	 * @param responseMessage
	 */
	private void sendMessage(int responseCode, String responseMessage) {
		onUploadProcessListener.onUploadDone(responseCode, responseMessage);
	}

	/**
	 * 下面是一个自定义的回调函数，用到回调上传文件是否完成
	 *
	 * @author shimingzheng
	 *
	 */
	public static interface OnUploadProcessListener {
		/**
		 * 上传响应
		 *
		 * @param responseCode
		 * @param message
		 */
		void onUploadDone(int responseCode, String message);

		/**
		 * 上传中
		 *
		 * @param uploadSize
		 */
		void onUploadProcess(int uploadSize);

		/**
		 * 准备上传
		 *
		 * @param fileSize
		 */
		void initUpload(int fileSize);
	}

	private OnUploadProcessListener onUploadProcessListener;

	public void setOnUploadProcessListener(
			OnUploadProcessListener onUploadProcessListener) {
		this.onUploadProcessListener = onUploadProcessListener;
	}

	public int getReadTimeOut() {
		return readTimeOut;
	}

	public void setReadTimeOut(int readTimeOut) {
		this.readTimeOut = readTimeOut;
	}

	public int getConnectTimeout() {
		return connectTimeout;
	}

	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	/**
	 * 获取上传使用的时间
	 *
	 * @return
	 */
	public static int getRequestTime() {
		return requestTime;
	}

	public static interface uploadProcessListener {

	}

	public static String getPhotoParams(String str) {
		File upfile = new File(str);
		if (upfile.exists()) {
			try {
				Bitmap bitmap = PictureShowUtils.getImageBitByPath(str);
				if (bitmap != null) {
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
					bitmap = null;
					System.gc();
					byte[] data = out.toByteArray();
					String string = Base64.encodeToString(data, Base64.DEFAULT);
					return string;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
