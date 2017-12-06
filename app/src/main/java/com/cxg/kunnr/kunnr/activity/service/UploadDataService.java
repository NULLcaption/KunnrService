package com.cxg.kunnr.kunnr.activity.service;



import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.cxg.kunnr.kunnr.activity.application.XPPApplication;
import com.cxg.kunnr.kunnr.activity.provider.DataProviderFactory;
import com.cxg.kunnr.kunnr.activity.query.PhotoInfo;

public class UploadDataService extends Service {

	protected static final int UPLOAD_PHOTO = 1;//
	protected static final int UPLOAD_ABNORMALPRICE = 2;
	protected static final int UPLOAD_DISTRIBUTION = 3;
	protected static final int UPLOAD_DISPLAY = 4;//
	protected static final int UPLOAD_INVENTORY = 5;
	protected static final int UPLOAD_MARKETCHECK = 6;
	protected static final int UPLOAD_ORDER = 7;
	protected static final int UPLOAD_CUSTSTOCK = 8;
	private static final int TIME_INTERNEL = 300000;
	protected static final int UPLOAD_FILE_DONE = 0;

	private NotificationManager mNotificationManager;
	private Handler uploadhandler = new Handler();

	private BroadcastReceiver UpdateListener = new BroadcastReceiver() {
		@SuppressWarnings("deprecation")
		public void onReceive(Context context, Intent intent) {
			Bundle b = intent.getExtras();
			String key = b.getString("type");
			String values = b.getString("custId");
			Message message;
			if (key.equals("photo")) {
				message = new Message();
				message.what = UPLOAD_PHOTO;
				message.obj = values;
				handler.sendMessage(message);
			}
		}
	};

	private Runnable runnable = new Runnable() {
		public void run() {
			new UpdateStatusTask().execute();
		}
	};

	public void onCreate() {
		super.onCreate();

		uploadhandler.postDelayed(runnable, TIME_INTERNEL);
		registerReceiver(UpdateListener, new IntentFilter(
				XPPApplication.UPLOADDATA_RECEIVER));
	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(final Message msg) {
			try {
				switch (msg.what) {
				case UPLOAD_PHOTO:
					new Thread() {
						public void run() {
							try {
								for (PhotoInfo photoInfo : PhotoInfo
										.synchronousPhoto((msg == null || msg.obj == null) ? null
												: (String) msg.obj)) {
									DataProviderFactory.getProvider()
											.uploadPicture(photoInfo);
								}
							} catch (Exception e) {
								Log.i("UploadDataService Error:", e.toString());
							}
						}
					}.start();
					break;
				}
				super.handleMessage(msg);
			} catch (Exception e) {
				Log.i("UploadDataService Error:", e.toString());
			}

		}

	};

	private class UpdateStatusTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			try {
				ConnectivityManager cwjManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo info = cwjManager.getActiveNetworkInfo();
				if (info != null && info.isAvailable()) {
					Message message = new Message();
					message.obj = null;
					message.what = UPLOAD_PHOTO;
					handler.sendMessage(message);
				}
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			uploadhandler.postDelayed(runnable, TIME_INTERNEL);
		}
	}

	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(UpdateListener);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

}
