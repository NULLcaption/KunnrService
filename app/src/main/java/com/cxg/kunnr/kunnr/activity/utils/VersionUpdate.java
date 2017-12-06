package com.cxg.kunnr.kunnr.activity.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Xml;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.Toast;

public class VersionUpdate {
	public Activity activity = null;
	private static final int UPDATA_CLIENT = 1;
	private static final int GET_UNDATAINFO_ERROR = 2;
	private static final int CANCEL_UPDATE = 3;

	private static final int DOWN_ERROR = 3;
	public   String strURL ;

	public VersionUpdate(Activity activity,String strURL) {
		this.activity = activity;
		this.strURL= strURL;
		// this.activity.onKeyDown(KeyEvent.KEYCODE_BACK, null);
		// downLoadApk();
		showUpdataDialog();
	}

	public boolean isUpdate() {
		return true;
	}

	/*
	 * 
	 * 弹出对话框通知用户更新程序
	 */
	protected void showUpdataDialog() {
		AlertDialog.Builder builer = new Builder(activity);
		builer.setTitle("版本升级");
		builer.setMessage("监测到新版本,请升级");
		// 当点确定按钮时从服务器上下载 新的apk 然后安装
		builer.setPositiveButton("确定", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				downLoadApk();
			}
		});
		builer.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface arg0) {
				// TODO Auto-generated method stub
//				showUpdataDialog();
			}

		});
		// 当点取消按钮时进行登录
		builer.setNegativeButton("取消", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		AlertDialog dialog = builer.create();
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
	}

	/*
	 * 从服务器中下载APK
	 */
	protected void downLoadApk() {
		final ProgressDialog pd; // 进度条对话框
		pd = new ProgressDialog(activity);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setMessage("正在下载更新");
		pd.setCanceledOnTouchOutside(false);
		pd.show();

		pd.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface arg0) {
				// TODO Auto-generated method stub
				showUpdataDialog();
			}

		});
		new Thread() {
			@Override
			public void run() {
				try {
					File file = getFileFromServer(strURL, pd); // TODO 下载url
					sleep(3000);
					installApk(file);
					pd.dismiss(); // 结束掉进度条对话框
				} catch (Exception e) {
					Message msg = new Message();
					msg.what = DOWN_ERROR;
					handler.sendMessage(msg);
					e.printStackTrace();
				}
			}
		}.start();

	}

	//
	public File getFileFromServer(String path, ProgressDialog pd)
			throws Exception {
		// 如果相等的话表示当前的sdcard挂载在手机上并且是可用的
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			// 获取到文件的大小
			pd.setMax(conn.getContentLength());
			InputStream is = conn.getInputStream();
			String downloadPath = Environment.getExternalStorageDirectory()
					+ "/XPP/download/";
			File dir = new File(downloadPath);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			File file = new File(downloadPath, "XPPMobileTerminal.apk");
			FileOutputStream fos = new FileOutputStream(file);
			BufferedInputStream bis = new BufferedInputStream(is);
			byte[] buffer = new byte[1024];
			int len;
			int total = 0;
			while ((len = bis.read(buffer)) != -1) {
				fos.write(buffer, 0, len);
				total += len;
				// 获取当前下载量
				pd.setProgress(total);
			}
			fos.close();
			bis.close();
			is.close();
			return file;
		} else {
			return null;
		}
	}

	// 安装apk
	protected void installApk(File file) {
		Intent intent = new Intent();
		// 执行动作
		intent.setAction(Intent.ACTION_VIEW);
		// 执行的数据类型
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		activity.startActivity(intent);
	}

	/*
	 * 进入程序的主界面
	 */
	// private void LoginMain(){
	// Intent intent = new Intent(activity,LoginActivity.class);
	// activity.startActivity(intent);
	// //结束掉当前的activity
	// activity.finish();
	// }

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
				case UPDATA_CLIENT:
					// 对话框通知用户升级程序
					showUpdataDialog();
					break;
				case GET_UNDATAINFO_ERROR:
					// 服务器超时
					Toast.makeText(activity, "获取服务器更新信息失败", 1).show();
					// LoginMain();
					break;
				// case DOWN_ERROR:
				// //下载apk失败
				// Toast.makeText(activity, "下载新版本失败", 1).show();
				// // LoginMain();
				// break;
				case CANCEL_UPDATE:

					break;
			}
		}
	};

}
