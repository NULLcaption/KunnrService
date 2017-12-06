package com.cxg.kunnr.kunnr.activity.provider;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.cxg.kunnr.kunnr.R;
import com.cxg.kunnr.kunnr.activity.activity.KunnrActivity;
import com.cxg.kunnr.kunnr.activity.activity.LoginActivity;

import org.apache.commons.lang.StringUtils;

/**
 * Description: 系统数据更新任务
 * author: xg.chen
 * time: 2017/11/21
 * version: 1.0
 */
public class UpdateTask extends AsyncTask<Object, Object, Integer> {
	private static final String TAG = "UpdateTask";
	private static final int TIMEOUT = 5000;
	private  NotificationManager mNotificationManager;
	private static final Long delay = 1000L;
	private static UpdateTask instance;
	private static int FIAL = 0;
	private static int SUCCESS = 1;
	private static int FIAL_SKU = 2;
	private static int FIAL_CHANNEL = 3;
	private static int FIAL_DICTIONARY = 4;
	private static int FIAL_TIME = 5;
	private static int FIAL_TIMEOUT = 6;
	private static int NEW_VERSION = 7;
	private static int FIAL_MENU = 8;
	private static int FIAL_ROUTE = 9;
	private static int FIAL_KunnrStockDate = 10;
	private static int FIAL_ORDER = 11;
	private static int FIAL_SKUUNIT = 12;
	private final Lock running = new ReentrantLock();
	private static Context context;
	private static boolean isRefresh ;//是否自动刷新页面

	public UpdateTask(Context context,boolean isRefresh) {
		UpdateTask.context = context;
		UpdateTask.isRefresh = isRefresh;
	}

	public static UpdateTask getInstance() {
		instance = new UpdateTask(context,isRefresh);
		return instance;
	}

	@Override
	protected void onPreExecute() {
		Log.d(TAG, "Starting Update Task");
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onPostExecute(Integer result) {

		Context ctx = DataProviderFactory.getContext();
		// 创建一个NotificationManager的引用
		mNotificationManager = (NotificationManager) ctx
				.getSystemService(Context.NOTIFICATION_SERVICE);
		String str = "数据同步完成....";
		switch (result) {
			case 0:
				str = "数据同步失败....";
				break;
			case 2:
				str = "sku同步失败....";
				break;
			case 3:
				str = "渠道信息同步失败....";
				break;
			case 4:
				str = "陈列数据同步失败....";
				break;
			case 5:
				str = "服务器时间同步失败....";
				break;
			case 6:
				str = "连接服务器超时....";
				break;
			case 7:
				str = "同步成功，监测到新版本......";
				break;
			case 8:
				str = "菜单同步失败.....";
				break;
			case 9:
				str = "线路同步失败.....";
				break;
			case 10:
				str = "库存上报时间同步失败.....";
				break;
			case 11:
				str = "订单同步失败.....";
				break;
			case 12:
				str = "门店分销量sku同步失败.....";
				break;
			default:
				break;
		}

        Notification.Builder builder1 = new Notification.Builder(ctx);
        builder1.setSmallIcon(R.drawable.notification_icon); //设置图标
        builder1.setTicker("香飘飘POD确认到货");
        builder1.setContentTitle("通知"); //设置标题
        builder1.setContentText("点击查看详细内容"); //消息内容
        builder1.setWhen(System.currentTimeMillis()); //发送时间
        builder1.setDefaults(Notification.DEFAULT_ALL); //设置默认的提示音，振动方式，灯光
        builder1.setAutoCancel(true);//打开程序后图标消失
        Intent intent =new Intent (ctx,KunnrActivity.class);
        PendingIntent pendingIntent =PendingIntent.getActivity(ctx, 0, intent, 0);
        builder1.setContentIntent(pendingIntent);
        Notification notification1 = builder1.build();
        mNotificationManager.notify(0, notification1);
		new Handler().postDelayed(new Runnable() {
			public void run() {
				mNotificationManager.cancel(0);
			}
		}, delay);

		instance = null;
	}

	@SuppressLint("SimpleDateFormat")
	@Override
	protected Integer doInBackground(Object... params) {
		running.lock();
		try {

			SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
			//设置服务器时间
			String result = DataProviderFactory.getProvider().getTime();
			if ("".equals(result)) {
				return FIAL_TIME;
			}
			//设置时间戳
			String day = f.format(new Date(Long.valueOf(result)));
			if(DataProviderFactory.getDayType()==null){
				DataProviderFactory.setDayType(day);
			}
			//设置特殊时期
			String status = DataProviderFactory.getProvider().getPhotoStatus();
			if (StringUtils.isNotEmpty(status)) {
				DataProviderFactory.setPhotoStatus(status);
			} else {
				return FIAL_TIME;
			}

			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return FIAL;
		} finally {
			running.unlock();
		}
	}

	public void waitTimeout() {
		try {
			running.tryLock(TIMEOUT, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
		}
	}


}