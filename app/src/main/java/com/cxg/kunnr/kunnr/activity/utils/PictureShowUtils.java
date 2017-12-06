package com.cxg.kunnr.kunnr.activity.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;


import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;
import android.text.TextUtils;

import com.cxg.kunnr.kunnr.activity.provider.DataProviderFactory;
import com.cxg.kunnr.kunnr.activity.query.PhotoInfo;

/**
 * Title: Pictures show tools class
 * Description: XPPMobileTerminal
 * @author: xg.chen
 */
public class PictureShowUtils {

	public static int pic_Hight = 600;
	public static int pic_Wight = 600;

	private String[] filesName = null;// 存储每张图片的路径

	public PictureShowUtils() {
		// R.string.upload_photo_size_hight;
		try {
			File file = new File(DataProviderFactory.getDirName);
			if (!file.exists()) { // 如果不存在则建立文件夹
				file.mkdirs(); // 创建文件夹
			}
			filesName = file.list();
		} catch (Exception e) {
			e.printStackTrace();
			filesName = null;
		}
	}

	public int count() {
		if (filesName != null) {
			return filesName.length;
		}
		return 0;
	}

	public String[] getFilesName() {
		return filesName;
	}

	public Bitmap getImageAt(int index) {
		String path = DataProviderFactory.getDirName;
		if (index > filesName.length) {
			return null;
		}
		path += filesName[index];
		if (getImageFile(path)) {
			File file = new File(path);
			BitmapFactory.Options opts = new BitmapFactory.Options();
			// 数字越大读出的图片占用的heap越小 不然总是溢出 压缩
			if (file.length() < 20480) { // 0-20k
				opts.inSampleSize = 1;
			} else if (file.length() < 51200) { // 20-50k
				opts.inSampleSize = 2;
			} else if (file.length() < 307200) { // 50-300k
				opts.inSampleSize = 4;
			} else if (file.length() < 819200) { // 300-800k
				opts.inSampleSize = 6;
			} else if (file.length() < 1048576) { // 800-1024k
				opts.inSampleSize = 8;
			} else {
				opts.inSampleSize = 10;
			}
			Bitmap sourceBitmap = BitmapFactory.decodeFile(path, opts);
			return sourceBitmap;
		}
		return null;
	}

	// add 20111031
	public String getfilePath(int index) {
		String path = DataProviderFactory.getDirName;
		if (index > filesName.length) {
			return null;
		}
		path += filesName[index];
		return path;
	}
	/**
	 * MethodsTitle: 获取图片的存放路径
	 * @author: xg.chen
	 * @date:2016年12月12日 上午11:51:59
	 * @version 1.0
	 * @param path
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap getImageBitByPath(String path, int width, int height) {
		if (getImageFile(path)) {
			BitmapFactory.Options opts = null;
			if (width > 0 && height > 0) {
				opts = new BitmapFactory.Options();
				opts.inJustDecodeBounds = true;
				BitmapFactory.decodeFile(path, opts);
				// 计算图片缩放比例
				final int minSideLength = Math.min(width, height);
				opts.inSampleSize = computeSampleSize(opts, minSideLength,
						width * height);
				opts.inJustDecodeBounds = false;
				opts.inInputShareable = true;
				opts.inPurgeable = true;
			}
			try {
				return Bitmap.createScaledBitmap(BitmapFactory.decodeFile(path, opts), width/4, width/4, true);
			} catch (OutOfMemoryError e) {
				e.printStackTrace();
			}
			return null;

		}
		return null;
	}

	public static Bitmap getImageBitByPath(String path, int i) {
		if (getImageFile(path)) {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true; // 设置了此属性一定要记得将值设置为false
			BitmapFactory.decodeFile(path, options);
			options.inSampleSize = 8;// computeSampleSize(options, -1, i);
			options.inJustDecodeBounds = false;
			Bitmap imageBitmap = BitmapFactory.decodeFile(path, options);
			if (imageBitmap != null) {
				return cutBmp(imageBitmap);
			}
			return null;
		}
		return null;
	}

	// 得到 压缩图

	public static Bitmap getImageBitByPath(String path) {
		if (getImageFile(path)) {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true; // 设置了此属性一定要记得将值设置为false
			BitmapFactory.decodeFile(path, options);
			options.inSampleSize = calculateInSampleSize(options, pic_Hight, pic_Wight);// computeSampleSize(options, -1,
			// pic_Hight*pic_wight);
			options.inJustDecodeBounds = false;
			Bitmap imageBitmap = BitmapFactory.decodeFile(path, options);
			return imageBitmap;
		}
		return null;
	}

	private static int calculateInSampleSize(BitmapFactory.Options options,
											 int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? widthRatio : heightRatio;
		}

		return inSampleSize;
	}


	public static int computeSampleSize(BitmapFactory.Options options,
										int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);
		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}
		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options,
												int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;
		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));
		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}
		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}
	/**
	 * MethodsTitle: 根据图片大小压缩后的
	 * @author: xg.chen
	 * @date:2017年3月28日 上午11:30:51
	 * @version 1.0
	 * @param path
	 * @return
	 */
	public static Bitmap getImage(String path) {// 根据图片大小压缩后的
		if (getImageFile(path)) {
			File file = new File(path);
			BitmapFactory.Options opts = new BitmapFactory.Options();
			// 数字越大读出的图片占用的heap越小 不然总是溢出
			if (file.length() < 20480) { // 0-20k
				opts.inSampleSize = 1;
			} else if (file.length() < 51200) { // 20-50k
				opts.inSampleSize = 2;
			} else if (file.length() < 307200) { // 50-300k
				opts.inSampleSize = 4;
			} else if (file.length() < 819200) { // 300-800k
				opts.inSampleSize = 6;
			} else if (file.length() < 1048576) { // 800-1024k
				opts.inSampleSize = 8;
			} else {
				opts.inSampleSize = 10;
			}
			Bitmap sourceBitmap = BitmapFactory.decodeFile(path, opts);
			return sourceBitmap;
		}
		return null;
	}

	public boolean deleteImageAt(int index) {
		String path = DataProviderFactory.getDirName;
		if (index > filesName.length) {
			return false;
		}
		path += filesName[index];
		if (getImageFile(path)) {
			File file = new File(path);
			if (file.exists()) {
				file.delete();
				return true;
			}
		}
		return false;
	}

	@SuppressLint("DefaultLocale")
	private static boolean getImageFile(String fName) {
		boolean re;
		String end = fName
				.substring(fName.lastIndexOf(".") + 1, fName.length())
				.toLowerCase();

		if (end.equals("jpg") || end.equals("gif") || end.equals("png")
				|| end.equals("jpeg") || end.equals("bmp")) {
			re = true;
		} else {
			re = false;
		}
		return re;
	}

	public static String getDirName() {
		File file = new File(DataProviderFactory.getDirName);
		if (!file.exists()) { // 如果不存在则建立文件夹
			file.mkdirs(); // 创建文件夹
		}
		return DataProviderFactory.getDirName;
	}
	/**
	 * 删除照片文件夹下所有照片
	 *
	 * */
	public static boolean deleAllPhoto() {
		File fileJia = new File(DataProviderFactory.getDirName);
		File[] childFiles = fileJia.listFiles();
		if (childFiles == null || childFiles.length == 0) {
			return true;
		}
		for (int i = 0; i < childFiles.length; i++) {
			if (childFiles[i].exists()) {
				childFiles[i].delete();
			}
		}
		return true;
	}

	public static boolean modayfyFileName(String oldFileName, String newFileName) {
		File oldFile = new File(DataProviderFactory.getDirName + oldFileName);
		oldFile.renameTo(new File(DataProviderFactory.getDirName + newFileName));
		return true;
	}

	public static Bitmap cutBmp(Bitmap bmp) {
		Bitmap result = null;
		try {
			int w = bmp.getWidth();// 输入长方形宽//123
			int h = bmp.getHeight();// 输入长方形高//164
			int nw;// 输出正方形宽
			if (w > h) {
				// 宽大于高
				nw = h;
				// result = Bitmap.createBitmap(bmp, (w - nw) / 2, 0, nw, nw);
				result = Bitmap.createBitmap(bmp, 20, 0, 120, 120);
			} else {
				// 高大于宽
				nw = w;
				// result = Bitmap.createBitmap(bmp, 0 , (h - nw) / 2, nw, nw);//20
				// ,123 ,123
				result = Bitmap.createBitmap(bmp, 0, 20, 120, 120);// 20 ,123 ,123
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 删除指定目录下文件及目录
	 *
	 * @param deleteThisPath
	 * @return
	 */
	public static void deleteFolderFile(String filePath, boolean deleteThisPath)
			throws IOException {

		if (!TextUtils.isEmpty(filePath)) {
			File file = new File(filePath);
			if (file.exists()) {
				if (file.isDirectory()) {// 处理目录
					File files[] = file.listFiles();
					for (int i = 0; i < files.length; i++) {
						deleteFolderFile(files[i].getAbsolutePath(), true);
					}
				}

				if (deleteThisPath) {
					if (!file.isDirectory()) {// 如果是文件，删除
						file.delete();
					} else {// 目录
						if (file.listFiles().length == 0) {// 目录下没有文件或者目录，删除
							file.delete();
						}
					}
				}
			}
		}
	}

	public static void deletePhoto(List<PhotoInfo> list) {
		for (PhotoInfo photoInfo : list) {
			deleteFile(DataProviderFactory.getDirName
					+ photoInfo.getPhotoName() + ".jpg");
		}
	}
	public static void deleteFile(String path) {
		File file = new File(path);
		try {
			if (file.exists()) { // 判断文件是否存在
				if (file.isFile()) { // 判断是否是文件
					file.delete(); // delete()方法 你应该知道 是删除的意思;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
