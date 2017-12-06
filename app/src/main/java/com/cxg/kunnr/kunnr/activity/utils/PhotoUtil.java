package com.cxg.kunnr.kunnr.activity.utils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;


import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.Bitmap.CompressFormat;
import android.util.Base64;
import android.util.Log;

import com.cxg.kunnr.kunnr.activity.application.XPPApplication;
import com.cxg.kunnr.kunnr.activity.provider.DataProviderFactory;
import com.cxg.kunnr.kunnr.activity.query.PhotoInfo;
import com.cxg.kunnr.kunnr.activity.query.UserInfo;

/**
 * Title: 手机拍照工具类
 * Description: XPPMobileTerminal
 * @author: xg.chen
 * @date:2016年12月12日 上午11:48:11
 */
@SuppressLint("SimpleDateFormat")
public class PhotoUtil {
	private final static int compressVal = 90;

	/**
	 * 根据图片名称得到图片的拍照时间
	 */
	public static String getpicTime(String picName) {
		return picName.substring(picName.lastIndexOf("XPP_") + 9,
				picName.length() - 4);
	}

	/**
	 * MethodsTitle: 获取时间戳，并创建图片名称
	 *
	 * @return
	 * @author: xg.chen
	 * @date:2017年3月28日 上午11:41:49
	 * @version 1.0
	 */
	public static String getphotoName() {
		String timestr = TimeUtil.getStringTime();
		if (timestr == null || timestr.equals("")) {
			return "XPP_" + getluanma(5, 1, 1, 1) + System.currentTimeMillis()
					+ getluanma(4, 0, 1, 1);
		}
		return "XPP_" + getluanma(5, 1, 1, 1) + timestr + getluanma(4, 0, 1, 1);
	}

	// 得到干扰码
	private static String getluanma(int zongshu, int fangxiang, int shuliang,
									int value) {
		Random ran = new Random();
		String str = "";
		if (fangxiang > 0) {
			for (int i = 0; i < zongshu - shuliang; i++) {
				int num = ran.nextInt(9) + 1;//
				str = str + num;
			}
			for (int i = 0; i < shuliang; i++) {
				str = str + value;
			}
		} else if (fangxiang < 0) {
			for (int i = 0; i < shuliang; i++) {
				str = str + value;
			}
			for (int i = 0; i < zongshu - shuliang; i++) {
				int num = ran.nextInt(9) + 1;//
				str = str + num;
			}
		} else {
			for (int i = 0; i < zongshu; i++) {
				int num = ran.nextInt(9) + 1;//
				str = str + num;
			}
		}
		return str;
	}

	public static String bitmapToString(Bitmap bitmap) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, compressVal, baos);

		byte[] b = baos.toByteArray();
		return Base64.encodeToString(b, Base64.DEFAULT);
	}

	/**
	 * 计算图片的缩放值 如果图片的原始高度或者宽度大与我们期望的宽度和高度，我们需要计算出缩放比例的数值。否则就不缩放。
	 * heightRatio是图片原始高度与压缩后高度的倍数， widthRatio是图片原始宽度与压缩后宽度的倍数。
	 * inSampleSize就是缩放值 ，取heightRatio与widthRatio中最小的值。
	 * inSampleSize为1表示宽度和高度不缩放，为2表示压缩后的宽度与高度为原来的1/2(图片为原1/4)。
	 *
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options,
											int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions(尺寸) larger than or equal to
			// the requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}

	/**
	 * 根据路径获得图片并压缩返回bitmap用于显示
	 *
	 * @return
	 */
	public static Bitmap getSmallBitmap(String filePath, int w, int h) {
		final BitmapFactory.Options options = new BitmapFactory.Options();

		// 该值设为true那么将不返回实际的bitmap不给其分配内存空间而里面只包括一些解码边界信息即图片大小信息
		options.inJustDecodeBounds = true;// inJustDecodeBounds设置为true，可以不把图片读到内存中,但依然可以计算出图片的大小
		BitmapFactory.decodeFile(filePath, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, w, h);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;// 重新读入图片，注意这次要把options.inJustDecodeBounds
		// 设为 false
		Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);// BitmapFactory.decodeFile()按指定大小取得图片缩略图

		return bitmap;
	}

	/**
	 * 保存到本地
	 *
	 * @param bitmap
	 */
	public static void saveBitmap(Bitmap bitmap, String savePath) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		File file = new File(savePath);
		try {
			file.createNewFile();
			BufferedOutputStream os = new BufferedOutputStream(
					new FileOutputStream(file));

			bitmap.compress(Bitmap.CompressFormat.JPEG, compressVal, baos);
			os.write(baos.toByteArray());

			os.flush();
			os.close();
			os = null;
			baos = null;
			file = null;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bitmap != null) {
				bitmap.recycle();
				bitmap = null;
				System.gc();
			}

		}
	}

	/**
	 * MethodsTitle: 拍照后为照片添加水印
	 *
	 * @param file
	 * @param ptInfo
	 * @return
	 * @author: xg.chen
	 * @date:2017年3月28日 下午2:15:22
	 * @version 1.0
	 */
	public static PhotoTask dealPhotoFile(final String file, PhotoInfo ptInfo) {
		PhotoTask task = new PhotoTask(file, ptInfo);
		task.start();
		return task;

	}

	/**
	 * Title: 多线程任务
	 * Description: XPPMobileTerminal
	 *
	 * @author: xg.chen
	 * @date:2017年3月28日 下午2:15:27
	 */
	public static class PhotoTask extends Thread {
		private String file;
		private PhotoInfo ptInfo;
		private boolean isFinished;

		public PhotoTask(String file, PhotoInfo ptInfo) {
			this.file = file;
			this.ptInfo = ptInfo;
		}

		@Override
		public void run() {
			BufferedOutputStream bos = null;
			Bitmap icon = null;
			try {
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inJustDecodeBounds = true;
				BitmapFactory.decodeFile(file, options); // 此时返回bm为空
				float percent =
						// options.outHeight > options.outWidth ? options.outHeight /
						// 960f : options.outWidth / 960f;
						options.outHeight > options.outWidth ? options.outHeight / 1440f
								: options.outWidth / 1440f;
				System.out.println("------Height:" + options.outHeight
						+ "-----width:" + options.outWidth);
				int fontsize = adjustFontSize(options.outWidth,
						options.outHeight);
				if (percent < 1) {
					percent = 1;
				}
				int width = (int) (options.outWidth / percent);
				int height = (int) (options.outHeight / percent);
				// int width = options.outWidth ;
				// int height = options.outHeight;
				icon = Bitmap
						.createBitmap(width, height, Bitmap.Config.RGB_565);

				// 初始化画布 绘制的图像到icon上
				Canvas canvas = new Canvas(icon);
				// 建立画笔
				Paint photoPaint = new Paint();
				// 获取跟清晰的图像采样
				photoPaint.setDither(true);
				// 过滤一些
				// photoPaint.setFilterBitmap(true);
				options.inJustDecodeBounds = false;

				Bitmap prePhoto = BitmapFactory.decodeFile(file);
				if (percent > 1) {
					prePhoto = Bitmap.createScaledBitmap(prePhoto, width,
							height, true);
				}

				canvas.drawBitmap(prePhoto, 0, 0, photoPaint);

				if (prePhoto != null && !prePhoto.isRecycled()) {
					prePhoto.recycle();
					prePhoto = null;
					System.gc();
				}

				// 设置画笔
				Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG
						| Paint.DEV_KERN_TEXT_FLAG);
				// 字体大小

				textPaint.setTextSize(fontsize);
				// 采用默认的宽度
				textPaint.setTypeface(Typeface.DEFAULT);
				// 采用的颜色
				textPaint.setColor(Color.YELLOW);
				// 阴影设置
				textPaint.setShadowLayer(3f * percent, 2f * percent,
						2f * percent, Color.DKGRAY);

				// 时间水印
				String mark = ptInfo.getPhototype()
						+ "    "
						+ UserInfo.findByLoginName(
						DataProviderFactory.getLoginName())
						.getUserName();
				float textWidth = textPaint.measureText(mark);
				canvas.drawText(mark, width - textWidth - 10, height - 36
						- fontsize, textPaint);
				mark = getCurrTime("yyyy-MM-dd HH:mm:ss", ptInfo.getTimestamp());
				textWidth = textPaint.measureText(mark);
				canvas.drawText(mark, width - textWidth - 10, height - 26,
						textPaint);
				if (XPPApplication.PhotoType.SIGNIN.equals(ptInfo.getPtype())
						|| XPPApplication.PhotoType.SIGNOUT.equals(ptInfo.getPtype())) {
					mark = ptInfo.getPhotoaddress();
					textWidth = textPaint.measureText(mark);
					canvas.drawText(mark, width - textWidth - 10, height - 116
							- fontsize, textPaint);
				}
				bos = new BufferedOutputStream(new FileOutputStream(file));

				int quaility = (int) (100 / percent > 80 ? 80 : 100 / percent);
				icon.compress(CompressFormat.JPEG, quaility, bos);
				bos.flush();
			} catch (Exception e) {
				Log.i("PhotoTask Error:", e.toString());
				if (bos != null) {

					try {
						bos.close();
					} catch (IOException e1) {
						Log.i("PhotoTask Error:", e1.toString());
					}
					bos = null;

				}
				if (icon != null && !icon.isRecycled()) {
					icon.recycle();
					icon = null;
					System.gc();
				}

			} finally {
				isFinished = true;
				if (bos != null) {
					try {
						bos.close();
						bos = null;
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (icon != null && !icon.isRecycled()) {
					icon.recycle();
					icon = null;
					System.gc();
				}
			}
		}
	}

	/**
	 * MethodsTitle: 格式化时间戳
	 *
	 * @param pattern
	 * @param Timestamp
	 * @return
	 * @author: xg.chen
	 * @date:2017年3月28日 下午2:17:21
	 * @version 1.0
	 */
	public static String getCurrTime(String pattern, String Timestamp) {
		if (pattern == null) {
			pattern = "yyyyMMddHHmmss";
		}
		if (Timestamp == null) {
			return (new SimpleDateFormat(pattern)).format(new Date());
		} else {
			return (new SimpleDateFormat(pattern)).format(new Date(Long
					.parseLong(Timestamp)));
		}

	}

	public static int adjustFontSize(int screenWidth, int screenHeight) {
		screenWidth = screenWidth > screenHeight ? screenWidth : screenHeight;
		/**
		 * 1. 在视图的 onsizechanged里获取视图宽度，一般情况下默认宽度是320，所以计算一个缩放比率 rate = (float)
		 * w/320 w是实际宽度 2.然后在设置字体尺寸时 paint.setTextSize((int)(8*rate));
		 * 8是在分辨率宽为320 下需要设置的字体大小 实际字体大小 = 默认字体大小 x rate
		 */
		int rate = (int) (5 * (float) screenWidth / 320); // 我自己测试这个倍数比较适合，当然你可以测试后再修改
		return rate < 30 ? 30 : rate; // 字体太小也不好看的
	}
}
