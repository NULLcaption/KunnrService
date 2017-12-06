package com.cxg.kunnr.kunnr.activity.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.util.Hashtable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Description: 图片下载
 * author: xg.chen
 * time: 2017/11/24
 * version: 1.0
 */

public class PhotoDownLoader {
    private static final String PhotoDownLoader_Log = Utils.makeLogTag(PhotoDownLoader.class);
    private Hashtable<String, Integer> taskCollection;
    private LruCache<String, Bitmap> lruCache;
    private ExecutorService threadPool;
    private File cacheFileDir;
    private static final String DIR_CACHE = "/";
    private static final long DIR_CACHE_LIMIT = 10 * 1024 * 1024;
    private static final int IMAGE_DOWNLOAD_FAIL_TIMES = 2;

    public PhotoDownLoader(Context context) {
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        lruCache = new LruCache<String, Bitmap>(maxMemory / 8) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight() / 1024;
            }
        };
        taskCollection = new Hashtable<>();
        threadPool = Executors.newFixedThreadPool(10);
        cacheFileDir = Utils.createFileDir(context, DIR_CACHE);
    }

    private void addLruCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null && bitmap != null) {
            lruCache.put(key, bitmap);
        }
    }

    private Bitmap getBitmapFromMemCache(String key) {
        return lruCache.get(key);
    }

    public void loadImage(final String url, final int width, final int height,
                          AsyncImageLoaderListener listener) {
        Log.i(PhotoDownLoader_Log, "download:" + url);
        final ImageHandler handler = new ImageHandler(listener);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = downloadImage(url, width, height);
                Message msg = handler.obtainMessage();
                msg.obj = bitmap;
                handler.sendMessage(msg);
                addLruCache(url, bitmap);
                long cacheFileSize = Utils.getFileSize(cacheFileDir);
                if (cacheFileSize > DIR_CACHE_LIMIT) {
                    Log.i(PhotoDownLoader_Log, cacheFileDir
                            + " size has exceed limit." + cacheFileSize);
                    Utils.delFile(cacheFileDir, false);
                    taskCollection.clear();
                }
                String urlKey = url.replaceAll("[^\\w]", "");
                Utils.savaBitmap(cacheFileDir, urlKey, bitmap);
            }
        };
        taskCollection.put(url, 0);
        threadPool.execute(runnable);
    }

    public Bitmap getBitmapCache(String url) {
        String urlKey = url.replaceAll("[^\\w]", "");
        if (getBitmapFromMemCache(url) != null) {
            return getBitmapFromMemCache(url);
        } else if (Utils.isFileExists(cacheFileDir, urlKey)
                && Utils.getFileSize(new File(cacheFileDir, urlKey)) > 0) {
            Bitmap bitmap = BitmapFactory.decodeFile(cacheFileDir.getPath()
                    + File.separator + urlKey);
            addLruCache(url, bitmap);
            return bitmap;
        }
        return null;
    }

    public synchronized void cancelTasks() {
        if (threadPool != null) {
            threadPool.shutdownNow();
            threadPool = null;
        }
    }

    public Hashtable<String, Integer> getTaskCollection() {
        return taskCollection;
    }

    private Bitmap downloadImage(String url, int width, int height) {
        Bitmap bitmap = null;
        HttpClient httpClient = new DefaultHttpClient();
        try {
            httpClient.getParams().setParameter(
                    CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
            HttpPost httpPost = new HttpPost(url);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = httpResponse.getEntity();
                byte[] byteIn = EntityUtils.toByteArray(entity);
                BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
                bmpFactoryOptions.inJustDecodeBounds = true;
                BitmapFactory.decodeByteArray(byteIn, 0, byteIn.length,
                        bmpFactoryOptions);
                int heightRatio = (int) Math.ceil(bmpFactoryOptions.outHeight
                        / height);
                int widthRatio = (int) Math.ceil(bmpFactoryOptions.outWidth
                        / width);
                if (heightRatio > 1 && widthRatio > 1) {
                    bmpFactoryOptions.inSampleSize = heightRatio > widthRatio ? heightRatio
                            : widthRatio;
                }
                bmpFactoryOptions.inJustDecodeBounds = false;
                bitmap = BitmapFactory.decodeByteArray(byteIn, 0,
                        byteIn.length, bmpFactoryOptions);
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (ConnectTimeoutException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (httpClient != null && httpClient.getConnectionManager() != null) {
                httpClient.getConnectionManager().shutdown();
            }
        }
        if (taskCollection.get(url) != null) {
            int times = taskCollection.get(url);
            if (bitmap == null
                    && times < IMAGE_DOWNLOAD_FAIL_TIMES) {
                times++;
                taskCollection.put(url, times);
                bitmap = downloadImage(url, width, height);
                Log.i(PhotoDownLoader_Log, "Re-download " + url + ":" + times);
            }
        }
        return bitmap;
    }

    /**
     * Description: 异步加载图片
     * author: xg.chen
     * time: 2017/11/24
     * version: 1.0
     */
    public interface AsyncImageLoaderListener {
        void onImageLoader(Bitmap bitmap);
    }

    /**
     * Description: 图片Handler
     * author: xg.chen
     * time: 2017/11/24
     * version: 1.0
     */
    static class ImageHandler extends Handler {

        private AsyncImageLoaderListener listener;

        public ImageHandler(AsyncImageLoaderListener listener) {
            this.listener = listener;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            listener.onImageLoader((Bitmap) msg.obj);
        }
    }
}
