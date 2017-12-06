package com.cxg.kunnr.kunnr.activity.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.cxg.kunnr.kunnr.R;
import com.cxg.kunnr.kunnr.activity.adapter.KunnrDetailAdapter;
import com.cxg.kunnr.kunnr.activity.application.XPPApplication;
import com.cxg.kunnr.kunnr.activity.entity.BaseDictionary;
import com.cxg.kunnr.kunnr.activity.entity.BaseParameter;
import com.cxg.kunnr.kunnr.activity.provider.DataProviderFactory;
import com.cxg.kunnr.kunnr.activity.query.KunnrPodDetail;
import com.cxg.kunnr.kunnr.activity.query.KunnrPodTotal;
import com.cxg.kunnr.kunnr.activity.query.PhotoInfo;
import com.cxg.kunnr.kunnr.activity.query.ResultMsgSap;
import com.cxg.kunnr.kunnr.activity.query.UserInfo;
import com.cxg.kunnr.kunnr.activity.query.ZsshipmentInTransit;
import com.cxg.kunnr.kunnr.activity.query.ZsshipmentInTransitH;
import com.cxg.kunnr.kunnr.activity.utils.DatePicker;
import com.baidu.location.LocationClientOption.LocationMode;
import com.cxg.kunnr.kunnr.activity.utils.ExitApplication;
import com.cxg.kunnr.kunnr.activity.utils.MyImageButton;
import com.cxg.kunnr.kunnr.activity.utils.MyUtil;
import com.cxg.kunnr.kunnr.activity.utils.PhotoUtil;
import com.cxg.kunnr.kunnr.activity.utils.PictureShowUtils;
import com.cxg.kunnr.kunnr.activity.utils.TimeUtil;
import com.cxg.kunnr.kunnr.activity.utils.UploadUtil;

import android.view.View.OnClickListener;
import android.widget.Toast;

import org.apache.commons.lang.StringUtils;
import org.ksoap2.SoapFault12;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Description: POD确认到货详细单
 * author: xg.chen
 * time: 2017/11/22
 * version: 1.0
 */

public class KunnrPodDetailActivity extends AppCompatActivity implements OnClickListener,
        UploadUtil.OnUploadProcessListener {

    private static final String TAG = "uploadImage";
    /**
     * 去上传文件
     */
    protected static final int TO_UPLOAD_FILE = 1;
    /**
     * 上传文件响应
     */
    protected static final int UPLOAD_FILE_DONE = 2; //
    /**
     * 选择文件
     */
    public static final int TO_SELECT_PHOTO = 3;
    /**
     * 上传初始化
     */
    private static final int UPLOAD_INIT_PROCESS = 4;
    /**
     * 上传中
     */
    private static final int UPLOAD_IN_PROCESS = 5;

    private TextView Tknum;
    private EditText confirmTime, warring, note;
    private ListView zsshipmentInTransitListView;
    private List<ZsshipmentInTransit> zsshipmentInTransitList_001;
    private List<ZsshipmentInTransit> zsshipmentInTransitList;
    private List<BaseDictionary> baseDictionaryList;
    private ZsshipmentInTransitH zsshipmentInTransitH;
    private KunnrDetailAdapter kunnrDetailAdapter;
    private Dialog overdialog;
    private Dialog waitingDialog;
    private Button ex, submitData, exit;
    private DatePicker confrimTimePicker;
    // 定位相关
    private TextView tv1, tv2, tv3;
    // 声明LocationClient类
    private LocationClient lc = null;
    private MyBaidulistener listener = null;
    // 设置图片路径
    private List<PhotoInfo> photoInfoList;
    private ProgressBar progressBar;
    private ProgressDialog progressDialog;
    private TableLayout table;
    private BaseParameter type;
    private String dir;
    private int width;
    private int height;
    private String photoNameAll;// 文件名
    private String callbaclResultMsg;//返回信息
    private static final int BAIDU_READ_PHONE_STATE =100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_kunnr_detail);

        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        width = display.getWidth();
        height = display.getHeight();

        //初始化视图
        initView();
        //初始化数据
        initData();
        //初始化经纬度以及详细地址，判断是否为android6.0系统版本，如果是，需要动态添加权限
        if (Build.VERSION.SDK_INT>=23){
            showContacts();
        }else{
            inidingwei();//init为定位方法
        }

        //设置异常分类信息为：无异常
        warring.setText("无异常");

        //置入一个不设防的VmPolicy
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }

        ExitApplication.getInstance().addActivity(this);
    }

    /**
     * Description: Android6.0申请权限的回调方法
     * author: xg.chen
     * time: 2017/12/4
     * version: 1.0
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            // requestCode即所声明的权限获取码，在checkSelfPermission时传入
            case BAIDU_READ_PHONE_STATE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 获取到权限，作相应处理（调用定位SDK应当确保相关权限均被授权，否则可能引起定位失败）
                    inidingwei();
                } else {
                    // 没有获取到权限，做特殊处理
                    Toast.makeText(getApplicationContext(), "获取位置权限失败，请手动开启", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    /**
     * Description: android6.0系统版本，如果是，需要动态添加权限
     * author: xg.chen
     * time: 2017/12/4
     * version: 1.0
     */
    public void showContacts(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(),"没有权限,请手动开启定位权限",Toast.LENGTH_SHORT).show();
            // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
            ActivityCompat.requestPermissions(KunnrPodDetailActivity.this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE}, BAIDU_READ_PHONE_STATE);
        }else{
            inidingwei();
        }
    }

    /**
     * Description: 初始化照片列表
     * author: xg.chen
     * time: 2017/11/27
     * version: 1.0
     */
    private void initPhotoTable() {
        table = new TableLayout(this);
        table = (TableLayout) findViewById(R.id.photoViewTable);
        XPPApplication.PhotoType photoType = XPPApplication.PhotoType.KUNNRYD;
        if (DataProviderFactory.getUserId() != null) {
            photoInfoList = PhotoInfo.findByShop(DataProviderFactory.getUserId(), photoType);
        } else {
            Toast.makeText(getApplicationContext(), "客户编号不存在，请清理缓存删除后重试！",
                    Toast.LENGTH_SHORT).show();
        }
        if (photoInfoList == null || photoInfoList.size() == 0) {
            List<PhotoInfo> photoInfoList = new ArrayList<>();
            new LoadImageAsyncTask().execute(photoInfoList);
        } else {
            new LoadImageAsyncTask().execute(photoInfoList);
        }
    }

    /**
     * Description: 异步加载图片
     * author: xg.chen
     * time: 2017/11/27
     * version: 1.0
     */
    public class LoadImageAsyncTask extends AsyncTask<List<PhotoInfo>, Integer, Map<String, Bitmap>> {
        List<PhotoInfo> photoList = new ArrayList<>();

        protected void onPreExecute() {
            showWaitingDialog();
        }

        @Override
        protected void onPostExecute(Map<String, Bitmap> maps) {
            table.removeAllViews();
            addRow(photoList, maps, table);
            dismissWaitingDialog();
        }

        @Override
        protected Map<String, Bitmap> doInBackground(List<PhotoInfo>... params) {
            photoList = params[0];
            return MyUtil.buildThum(photoList, width, height);
        }
    }

    /**
     * Description: 添加图片
     * author: xg.chen
     * time: 2017/11/27
     * version: 1.0
     */
    public void addRow(List<PhotoInfo> photoList, Map<String, Bitmap> picMap,
                       TableLayout table) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int size = photoList.size();
        int rowCount = size % 3;// 多余
        int row_count = 0;// 总行数
        if (rowCount != 0) {
            row_count = size / 3 + 1;
        } else {
            row_count = size / 3;
        }
        int j = 0;
        TableRow row = new TableRow(this);
        if (size != 0) {
            for (int i = 0; i < size; i++) {
                MyImageButton b = new MyImageButton(this);
                b.setMaxWidth((int) (width * 0.4));
                b.setMaxHeight((int) (height * 0.2));
                dir = DataProviderFactory.getDirName
                        + photoList.get(i).getPhotoName() + ".jpg";
                Long timeL = Long.parseLong(PhotoUtil.getpicTime(photoList.get(
                        i).getPhotoName()));
                String time = f.format(new Date(timeL));
                b.setText(time);
                b.setTag(R.string.tag1, dir);
                b.setTag(R.string.tag2, photoList.get(i).getPhotoName());
                if (XPPApplication.Status.FINISHED.equals(photoList.get(i).getStatus())) {
                    b.getBackground().setAlpha(0);// 去掉边框
                }
                b.setImageBitmap(picMap.get(dir));
                b.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                b.setColor(Color.BLACK);
                b.setOnClickListener(photoItemClick);
                b.setOnLongClickListener(photoLongClick);
                row.addView(b);
                if ((i + 1) % 3 == 0) {
                    j++;
                    table.addView(row);
                    row = new TableRow(this);
                    if (size == i + 1) {
                        TableRow row1 = new TableRow(this);
                        addBtnRow(row1, table);
                    }

                } else if (rowCount != 0 && j + 1 == row_count) {
                    if (i == size - 1) {
                        if (size == i + 1) {
                            addBtnRow(row, table);
                        } else {
                            table.addView(row);
                        }
                    }
                }
            }
        } else {
            TableRow row1 = new TableRow(this);
            addBtnRow(row1, table);
        }
    }

    /**
     * Description: Dispatch incoming result to the correct fragment.
     * author: xg.chen
     * time: 2017/11/27
     * version: 1.0
     */
    @SuppressWarnings("deprecation")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            if (Integer.valueOf(android.os.Build.VERSION.SDK) >= 5) {
                overridePendingTransition(R.anim.push_right_in,
                        R.anim.push_right_out);
            }
            if (photoNameAll != null) {
                /** 记录照片信息 */
                PhotoInfo ptInfo = new PhotoInfo();
                ptInfo.setPhotoName(photoNameAll);
                ptInfo.setPtype(XPPApplication.PhotoType.KUNNRYD);
                ptInfo.setStatus(XPPApplication.Status.NEW);
                ptInfo.setEmplid(DataProviderFactory.getUserId());
                ptInfo.setCustid(DataProviderFactory.getUserId());//// 运单编号
                String loginname = DataProviderFactory.getLoginName();//登录账号
                ptInfo.setCustName(UserInfo.findByLoginName(loginname).getUserName());//经销商名称
                ptInfo.setActid("1");
                ptInfo.setTimestamp(TimeUtil.getStringTime());//设置时间戳--直接获取服务器时间
                ptInfo.setSeq(Tknum.getText().toString());// 运单编号
                ptInfo.setDayType(DataProviderFactory.getDayType());
                String filestr = PictureShowUtils.getDirName() + photoNameAll + ".jpg";
                ptInfo.setPhototype("运单照");
                Thread task = PhotoUtil.dealPhotoFile(filestr, ptInfo);
                boolean b = PhotoInfo.save(ptInfo);
                /** 压缩 **/
                try {
                    task.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                initPhotoTable();
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            if (Integer.valueOf(android.os.Build.VERSION.SDK) >= 5) {
                overridePendingTransition(R.anim.push_right_in,
                        R.anim.push_right_out);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Description: 添加图片
     * author: xg.chen
     * time: 2017/11/27
     * version: 1.0
     */
    public void addBtnRow(TableRow row, TableLayout table) {
        ImageButton btn = new ImageButton(this);
        btn.getBackground().setAlpha(0);
        btn.setImageResource(R.drawable.bg_takephoto);
        btn.setMaxWidth((int) (width * 0.4));
        btn.setMaxHeight((int) (height * 0.2));
        btn.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                gotoPZ();
            }
        });
        row.addView(btn);
        table.addView(row);
    }

    /**
     * Description: 调取相机拍照
     * author: xg.chen
     * time: 2017/11/27
     * version: 1.0
     */
    private void gotoPZ() {
        photoNameAll = PhotoUtil.getphotoName();// 获取照片名字
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
                PictureShowUtils.getDirName(), photoNameAll + ".jpg")));
        startActivityForResult(intent, 0);
        if (Integer.valueOf(android.os.Build.VERSION.SDK) >= 5) {
            overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        }
    }

    /**
     * Description: 点击查看照片
     * author: xg.chen
     * time: 2017/11/27
     * version: 1.0
     */
    private OnClickListener photoItemClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            final MyImageButton b = (MyImageButton) v;
            final String p = (String) b.getTag(R.string.tag1);
            Intent intent = new Intent(KunnrPodDetailActivity.this,
                    ShowImageActivity.class);
            intent.putExtra("dir", p);
            startActivityForResult(intent, 213);
        }
    };

    /**
     * Description: 照片长按触发器
     * author: xg.chen
     * time: 2017/11/27
     * version: 1.0
     */
    private View.OnLongClickListener photoLongClick = new View.OnLongClickListener() {

        public boolean onLongClick(final View arg0) {
            View overdiaView = View.inflate(KunnrPodDetailActivity.this,
                    R.layout.dialog_confirmation, null);
            final Dialog overdialog = new Dialog(KunnrPodDetailActivity.this,
                    R.style.dialog_xw);
            overdialog.setContentView(overdiaView);
            overdialog.setCanceledOnTouchOutside(false);
            Button overcancel = (Button) overdiaView
                    .findViewById(R.id.dialog_cancel_btn);
            overcancel.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    overdialog.cancel();

                }
            });
            Button overok = (Button) overdiaView
                    .findViewById(R.id.dialog_ok_btn);
            overok.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    final MyImageButton b = (MyImageButton) arg0;
                    final String pName = (String) b.getTag(R.string.tag2);
                    final String path = (String) b.getTag(R.string.tag1);
                    File file = new File(path);
                    if (file.exists()) {
                        file.delete();
                        PhotoInfo.delete(PhotoInfo.getByPhotoName(pName));
                        initPhotoTable();
                    }
                    overdialog.cancel();
                }
            });
            overdialog.show();
            return false;
        }
    };

    /**
     * Description: 定位
     * author: xg.chen
     * time: 2017/11/27
     * version: 1.0
     */
    private void inidingwei() {
        lc = new LocationClient(getApplicationContext());
        listener = new MyBaidulistener();
        lc.registerLocationListener(listener);// 注册监听函数
        setviews();
        lc.start();
    }

    /**
     * Description:设置定位模式
     * author: xg.chen
     * time: 2017/11/27
     * version: 1.0
     */
    public void setviews() {
        LocationClientOption option = new LocationClientOption();

        option.setLocationMode(LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span=5000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集

        lc.setLocOption(option);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submitData:// 上传
                if (photoInfoList == null || photoInfoList.size() == 0) {
                    Toast.makeText(getApplicationContext(), "先拍照在保存",
                            Toast.LENGTH_SHORT).show();
                    break;
                }
                handler.sendEmptyMessage(TO_UPLOAD_FILE);
                for (PhotoInfo photoInfo : photoInfoList) {
                    PhotoInfo.submitPhoto(photoInfo);
                }
                UploadImageCustAsyncTask uploadImageCustAsyncTask = new UploadImageCustAsyncTask();
                uploadImageCustAsyncTask.execute(photoInfoList);
                break;
            case R.id.exit:// 返回home
                XPPApplication.exit(KunnrPodDetailActivity.this);
                break;
            default:
                break;
        }
    }

    /**
     * Description: 上传服务器响应回调
     * author: xg.chen
     * time: 2017/11/27
     * version: 1.0
     */
    @Override
    public void onUploadDone(int responseCode, String message) {
        progressDialog.dismiss();
        Message msg = Message.obtain();
        msg.what = UPLOAD_FILE_DONE;
        msg.arg1 = responseCode;
        msg.obj = message;
        handler.sendMessage(msg);
    }

    /**
     * Description: 文件上传过程
     * author: xg.chen
     * time: 2017/11/27
     * version: 1.0
     */
    @Override
    public void onUploadProcess(int uploadSize) {
        Message msg = Message.obtain();
        msg.what = UPLOAD_IN_PROCESS;
        msg.arg1 = uploadSize;
        handler.sendMessage(msg);
    }

    /**
     * Description: 初始化文件上传
     * author: xg.chen
     * time: 2017/11/27
     * version: 1.0
     */
    @Override
    public void initUpload(int fileSize) {
        Message msg = Message.obtain();
        msg.what = UPLOAD_INIT_PROCESS;
        msg.arg1 = fileSize;
        handler.sendMessage(msg);
    }

    /**
     * Description: 设置上传照片的信息
     * author: xg.chen
     * time: 2017/11/27
     * version: 1.0
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TO_UPLOAD_FILE:// 上传照片
                    handler.sendEmptyMessage(UPLOAD_FILE_DONE);
                    break;
                case UPLOAD_INIT_PROCESS:
                    progressBar.setMax(msg.arg1);
                    break;
                case UPLOAD_IN_PROCESS:
                    progressBar.setProgress(msg.arg1);
                    break;
                case UPLOAD_FILE_DONE:
                    sendNotice();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }

    };

    /**
     * Description: 上传完成后发送注意信息
     * author: xg.chen
     * time: 2017/11/27
     * version: 1.0
     */
    private void sendNotice() {
        Map<String, String> map = new HashMap<>();
        map.put("type", "photo");
        map.put("custId", DataProviderFactory.getUserId());
        XPPApplication.sendChangeBroad(KunnrPodDetailActivity.this,
                XPPApplication.UPLOADDATA_RECEIVER, map);
        XPPApplication.exit(KunnrPodDetailActivity.this);
    }

    /**
     * Description: 实现BDLocationListener接口
     * author: xg.chen
     * time: 2017/11/27
     * version: 1.0
     */
    public class MyBaidulistener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location != null) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                tv1.setText("" + location.getProvince() + ""
                        + location.getCity() + "" + location.getDistrict() + ""
                        + location.getStreet() + ""
                        + location.getStreetNumber());
                tv2.setText("" + longitude);
                tv3.setText("" + latitude);
                if (lc.isStarted()) {
                    // 获得位置之后停止定位
                    lc.stop();
                }
                lc = null;
                System.gc();
            } else {
                System.out.println("address:" + location.getProvince());
                tv1.setText("");
                tv2.setText("");
                tv3.setText("");
            }
        }
    }

    /**
     * Description: 初始化页面
     * author: xg.chen
     * time: 2017/11/23
     * version: 1.0
     */
    public void initView() {
        //运单号
        Tknum = (TextView) findViewById(R.id.Tknum);
        //运单明细
        zsshipmentInTransitListView = (ListView) findViewById(R.id.lv_sysinfo_detail);
        //确认到货时间
        confirmTime = (EditText) findViewById(R.id.confirmTime);
        confirmTime.setOnClickListener(BtnClicked);
        //异常分类
        warring = (EditText) findViewById(R.id.warring);
        warring.setOnClickListener(BtnClicked);
        //经纬度详细信息
        tv1 = (TextView) findViewById(R.id.dizhi_mingxi);
        tv2 = (TextView) findViewById(R.id.tvs_jingdu);
        tv3 = (TextView) findViewById(R.id.tvs_weidu);
        //备注
        note = (EditText) findViewById(R.id.note);

        findViewById(R.id.submitData).setOnClickListener(BtnClicked);
        findViewById(R.id.ex).setOnClickListener(BtnClicked);
        findViewById(R.id.exit).setOnClickListener(BtnClicked);
    }

    /**
     * Description: 初始化数据
     * author: xg.chen
     * time: 2017/11/23
     * version: 1.0
     */
    public void initData() {
        Bundle bun = getIntent().getExtras();
        if (bun != null) {
            zsshipmentInTransitH = (ZsshipmentInTransitH) bun.get("zsshipmentInTransitH");
        }
        if (zsshipmentInTransitH != null) {
            Tknum.setText(zsshipmentInTransitH.getTknum());
        }

        //根据运单编号获取POD明细信息
        new getPodDetailInfoTask().execute(Tknum.getText().toString());

        //加载时间控件
        selectDatePicker();

        //初始化照片列表
        initPhotoTable();
    }

    /**
     * Description: 记载详细列表
     * author: xg.chen
     * time: 2017/11/23
     * version: 1.0
     */
    public void initKunnrDetailData() {
        if (zsshipmentInTransitList == null) {
            zsshipmentInTransitList_001 = new ArrayList<>();
        } else if (zsshipmentInTransitList.size() != 0) {
            kunnrDetailAdapter = new KunnrDetailAdapter(zsshipmentInTransitList, this);
            zsshipmentInTransitListView.setAdapter(kunnrDetailAdapter);
        }
    }

    /**
     * Description: 根据运单编号获取POD明细信息
     * author: xg.chen
     * time: 2017/11/23
     * version: 1.0
     */
    private class getPodDetailInfoTask extends AsyncTask<String, Integer, List<ZsshipmentInTransit>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showWaitingDialog();
        }

        @Override
        protected List<ZsshipmentInTransit> doInBackground(String... params) {
            String string = params[0];
            return DataProviderFactory.getProvider().getPodDetailInfoTask(string);
        }

        @Override
        protected void onPostExecute(List<ZsshipmentInTransit> result) {
            dismissWaitingDialog();
            if (result.size() != 0) {
                zsshipmentInTransitList = result;
                //在EXP中创建获取到的在途运单明细数据
                creatKunnrPodDetail(zsshipmentInTransitList);
            }
        }
    }

    /**
     * Description: 在EXP中创建获取到的在途运单明细数据
     * author: xg.chen
     * time: 2017/11/28
     * version: 1.0
     */
    public void creatKunnrPodDetail(List<ZsshipmentInTransit> list) {
        try {
            if (list.size() != 0) {
                for (ZsshipmentInTransit zss : list) {
                    KunnrPodDetail kunnrPodDetail = new KunnrPodDetail();
                    kunnrPodDetail.setWaybillId(zsshipmentInTransitH.getTknum());
                    kunnrPodDetail.setMvgr1(zss.getMaktx());
                    kunnrPodDetail.setLfimg(zss.getLfimg());
                    kunnrPodDetail.setUnit(zss.getVrkme());
                    kunnrPodDetail.setCount(String.valueOf(list.size()));

                    new creatKunnrPodDetailTask().execute(kunnrPodDetail);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Description: 异步在EXP中创建获取到的在途运单明细数据
     * author: xg.chen
     * time: 2017/11/28
     * version: 1.0
     */
    private class creatKunnrPodDetailTask extends AsyncTask<KunnrPodDetail, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(KunnrPodDetail... params) {
            KunnrPodDetail kunnrPodDetail = params[0];
            return DataProviderFactory.getProvider().creatKunnrPodDetailTask(kunnrPodDetail);
        }

        @Override
        protected void onPostExecute(String result) {
            if (StringUtils.isNotEmpty(result)) {
                if (result.equals("SUCCESS")) {
                    //添加数据成功以后显示数据列表
                    initKunnrDetailData();
                } else {
                    Toast.makeText(getApplicationContext(), "保存明细数据数据出错！" + result, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    /**
     * Description: 按钮事件监听
     * author: xg.chen
     * time: 2017/11/23
     * version: 1.0
     */
    private View.OnClickListener BtnClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                //查看照片样例
                case R.id.ex:
                    Intent i = new Intent(KunnrPodDetailActivity.this,
                            PhotoOtherActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.in_from_right,
                            R.anim.out_to_left);
                    break;
                //提交数据
                case R.id.submitData:
                    //1、判断POD提报时间是否在提报时间内；
                    //条件：判断POD日期必须>当前月1号，<=当前日期。
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    // 获取前月的第一天
                    Calendar cale;
                    String firstday, nowTime;
                    cale = Calendar.getInstance();
                    cale.add(Calendar.MONTH, 0);
                    cale.set(Calendar.DAY_OF_MONTH, 1);
                    firstday = format.format(cale.getTime());
                    //获取当前时间
                    nowTime = format.format(new Date());
                    //判断时间
                    String checkTime = confirmTime.getText().toString();
                    if (checkTime.compareTo(firstday) > 0
                            && checkTime.compareTo(nowTime) <= 0
                            || checkTime.compareTo(firstday) == 0) {
                        System.out.println("++++++++++++++++>>>在提报时间内！！");
                        //2、判断是否为特殊时间（N为非必传,Y为必传）
                        String photoStatus = DataProviderFactory.getPhotoStatus();
                        if (photoStatus.equals("Y")) {
                            System.out.println("++++++++++++++++>>>在非特殊时间POD确认！！");
                            //3、先检查是否拍照
                            if (photoInfoList == null || photoInfoList.size() == 0) {
                                Toast.makeText(getApplicationContext(), "请先先拍照在提交确认",
                                        Toast.LENGTH_SHORT).show();
                                break;
                            } else {
                                System.out.println("++++++++++++++++>>>上传照片并更新数据！！");
                                try {
                                    String s = updateKunnrPodTotal("A");
                                    System.out.println("==============>>s:" + s);
                                    if (s.equals("SUCCESS")) {
                                        System.out.println("==============>>SAP做POD确认到货:OK");
                                        Toast.makeText(getApplicationContext(), "SAP做POD确认到货:OK", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "POD到货数据更新时失败：" + s, Toast.LENGTH_SHORT).show();
                                    }
                                    Thread.sleep(50);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else if (photoStatus.equals("N")) {
                            System.out.println("++++++++++++++++>>>在特殊时间POD确认！！");
                            //3、先去EXP更新数据
                            try {
                                String s = updateKunnrPodTotal_N("A");
                                System.out.println("==============>>s:" + s);
                                if (s.equals("SUCCESS")) {
                                    System.out.println("==============>>SAP做POD确认到货:OK");
                                    Toast.makeText(getApplicationContext(), "SAP做POD确认到货:OK", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "POD到货数据更新时失败：" + s, Toast.LENGTH_SHORT).show();
                                }
                                Thread.sleep(50);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "POD到货明细不在提报时间内,请检查后重新提交!", Toast.LENGTH_SHORT).show();
                    }
                    break;
                //返回按钮
                case R.id.exit:
                    XPPApplication.exit(KunnrPodDetailActivity.this);
                    break;
                //确认到货时间
                case R.id.confirmTime:
                    confrimTimePicker.show(confirmTime.getText().toString());
                    break;
                //异常信息分类
                case R.id.warring:
                    new getWarringInfoTask().execute();
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * Description: 异步上传照片
     * author: xg.chen
     * time: 2017/11/27
     * version: 1.0
     */
    public class UploadImageCustAsyncTask extends AsyncTask<List<PhotoInfo>, Integer, String> {
        String result = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showWaitingDialog();
        }

        @Override
        protected String doInBackground(List<PhotoInfo>... params) {
            if (photoInfoList.size() >= 1) {
                for (PhotoInfo photoInfo : photoInfoList) {
                    result = DataProviderFactory.getProvider()
                            .uploadPicture(photoInfo);
                }
            } else {
                Toast.makeText(getApplicationContext(), "先拍照在保存",
                        Toast.LENGTH_SHORT).show();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            dismissWaitingDialog();
            if (StringUtils.isNotEmpty(result)) {
                callbaclResultMsg = result;
                if (callbaclResultMsg.equals("SUCCESS")) {
                    System.out.println("++++++++++++>>callbaclResultMsg:" + callbaclResultMsg);
                    try {
                        kunnrPodConfirmSap();
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //创建pod照片存放列表
                    //creatKunnrPodFile();
                } else {
                    Toast.makeText(getApplicationContext(), "系统更新数据失败！" + callbaclResultMsg, Toast.LENGTH_SHORT).show();
                }
            }

        }

    }

    /**
     * Description: 在EXP中更新存储的该订单的状态
     * author: xg.chen
     * time: 2017/11/29
     * version: 1.0
     */
    public String updateKunnrPodTotal(String flag) {
        String s = "";
        try {
            KunnrPodTotal kunnrPodTotal = new KunnrPodTotal();

            kunnrPodTotal.setWaybillId(Tknum.getText().toString());//运单号
            kunnrPodTotal.setKunnr(DataProviderFactory.getCouldId());//经销商编号
            kunnrPodTotal.setArrivalDate(confirmTime.getText().toString());//确认到货时间
            kunnrPodTotal.setFlag(flag);//POD确认状态
            kunnrPodTotal.setStatus(DataProviderFactory.getPhotoStatus());//回单状态
            if (StringUtils.isNotEmpty(tv1.getText().toString())
                    || StringUtils.isNotEmpty(tv2.getText().toString())
                    || StringUtils.isNotEmpty(tv3.getText().toString())) {//详细地址经纬度
                kunnrPodTotal.setAddress(tv1.getText().toString());
                kunnrPodTotal.setLongitude(tv2.getText().toString());
                kunnrPodTotal.setLatitude(tv3.getText().toString());
            } else {
                return "请在手机应用管理中打开该应用的获取位置的权限！";
            }
            if (StringUtils.isNotEmpty(warring.getText().toString())) {//异常信息
                kunnrPodTotal.setAbnormal(warring.getText().toString());
            } else {
                return "请选择异常信息分类！";
            }
            if (StringUtils.isNotEmpty(note.getText().toString())) {//备注
                kunnrPodTotal.setRemark(note.getText().toString());
            }
            //上传照片
            final String[] s1 = {""};
            new Thread() {
                public void run() {
                    try {
                        for (PhotoInfo photoInfo : photoInfoList) {
                            s1[0] = DataProviderFactory.getProvider().uploadPicture(photoInfo);
                        }
                    } catch (Exception e) {
                        Log.i("UploadDataService Error:", e.toString());
                    }
                }
            }.start();
            System.out.println("+++++++++++++++>>" + s1);
            //在EXP中更新存储的该订单
            try {
                new updateKunnrPodTotalTask().execute(kunnrPodTotal);
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "SUCCESS";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }

    /**
     * Description: 在EXP中更新存储的该订单的状态
     * author: xg.chen
     * time: 2017/11/29
     * version: 1.0
     */
    public String updateKunnrPodTotal_N(String flag) {
        String s = "";
        try {
            KunnrPodTotal kunnrPodTotal = new KunnrPodTotal();

            kunnrPodTotal.setWaybillId(Tknum.getText().toString());//运单号
            kunnrPodTotal.setKunnr(DataProviderFactory.getCouldId());//经销商编号
            kunnrPodTotal.setArrivalDate(confirmTime.getText().toString());//确认到货时间
            kunnrPodTotal.setFlag(flag);//POD确认状态
            kunnrPodTotal.setStatus(DataProviderFactory.getPhotoStatus());//回单状态
            if (StringUtils.isNotEmpty(tv1.getText().toString())
                    || StringUtils.isNotEmpty(tv2.getText().toString())
                    || StringUtils.isNotEmpty(tv3.getText().toString())) {//详细地址经纬度
                kunnrPodTotal.setAddress(tv1.getText().toString());
                kunnrPodTotal.setLongitude(tv2.getText().toString());
                kunnrPodTotal.setLatitude(tv3.getText().toString());
            } else {
                return "请在手机应用管理中打开该应用的获取位置的权限！";
            }
            if (StringUtils.isNotEmpty(warring.getText().toString())) {//异常信息
                kunnrPodTotal.setAbnormal(warring.getText().toString());
            } else {
                return "请选择异常信息分类！";
            }
            if (StringUtils.isNotEmpty(note.getText().toString())) {//备注
                kunnrPodTotal.setRemark(note.getText().toString());
            }
            //在EXP中更新存储的该订单
            try {
                new updateKunnrPodTotalTask().execute(kunnrPodTotal);
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "SUCCESS";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }

    /**
     * Description: 异步请求EXP中更新存储的该订单
     * author: xg.chen
     * time: 2017/11/29
     * version: 1.0
     */
    private class updateKunnrPodTotalTask extends AsyncTask<KunnrPodTotal, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showWaitingDialog();
        }

        @Override
        protected String doInBackground(KunnrPodTotal... params) {
            KunnrPodTotal kunnrPodTotal = params[0];
            return DataProviderFactory.getProvider().updateKunnrPodTotalTask(kunnrPodTotal);
        }

        @Override
        protected void onPostExecute(String result) {
            dismissWaitingDialog();
            if (StringUtils.isNotEmpty(result)) {
                callbaclResultMsg = result;
                if (callbaclResultMsg.equals("SUCCESS")) {
                    System.out.println("++++++++++++>>callbaclResultMsg:" + callbaclResultMsg);
                    try {
                        kunnrPodConfirmSap();
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "系统更新数据失败！" + callbaclResultMsg, Toast.LENGTH_SHORT).show();
                }
            }

        }
    }

    /**
     * Description: 更新数据后去SAP做POD确认到货
     * author: xg.chen
     * time: 2017/11/29
     * version: 1.0
     */
    public void kunnrPodConfirmSap() {
        try {
            KunnrPodTotal kunnrPodTotal = new KunnrPodTotal();
            kunnrPodTotal.setWaybillId(Tknum.getText().toString());//运单号
            kunnrPodTotal.setArrivalDate(confirmTime.getText().toString());//确认到货时间
            try {
                new kunnrPodConfirmSapTask().execute(kunnrPodTotal);
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Description: 在SAP中做POD确认到货
     * author: xg.chen
     * time: 2017/11/29
     * version: 1.0
     */
    private class kunnrPodConfirmSapTask extends AsyncTask<KunnrPodTotal, Integer, ResultMsgSap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showWaitingDialog();
        }

        @Override
        protected ResultMsgSap doInBackground(KunnrPodTotal... params) {
            KunnrPodTotal KunnrPodTotal = params[0];
            return DataProviderFactory.getProvider().kunnrPodConfirmSapTask(KunnrPodTotal);
        }

        @Override
        protected void onPostExecute(ResultMsgSap result) {
            dismissWaitingDialog();
            if (result != null) {
                System.out.println("+++++++++++++>>" + result.getEvCode());
                if (result.getEvCode().equals("0")) {//成功:在EXP中更新存储的该订单的状态为C，更新其他相关信息
                    System.out.println("+++++++++++++>>成功:在EXP中更新存储的该订单的状态为C:" + result.getEvCode());
                    try {
                        updateKunnrPodTotalStatus("C");
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else if (result.getEvCode().equals("1")) {//不成功：在EXP中更新存储的该订单的状态为E，更新其他相关信息
                    try {
                        System.out.println("+++++++++++++>>不成功：在EXP中更新存储的该订单的状态为E:" + result.getEvCode());
                        updateKunnrPodTotalStatus("E");
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                Toast.makeText(getApplicationContext(), "POD到货明细在SAP中确认到货失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Description:  修改状态
     * author: xg.chen
     * time: 2017/11/30
     * version: 1.0
     */
    public void updateKunnrPodTotalStatus(String flag) {
        try {
            KunnrPodTotal kunnrPodTotal = new KunnrPodTotal();

            kunnrPodTotal.setWaybillId(Tknum.getText().toString());//运单号
            kunnrPodTotal.setKunnr(DataProviderFactory.getCouldId());//经销商编号
            kunnrPodTotal.setFlag(flag);//POD确认状态
            kunnrPodTotal.setCreator(DataProviderFactory.getUserId());

            try {
                new updateKunnrPodTotalStatusTask().execute(kunnrPodTotal);
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Description: 异步线程调整POD状态
     * author: xg.chen
     * time: 2017/11/30
     * version: 1.0
     */
    private class updateKunnrPodTotalStatusTask extends AsyncTask<KunnrPodTotal, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(KunnrPodTotal... params) {
            KunnrPodTotal kunnrPodTotal = params[0];
            return DataProviderFactory.getProvider().updateKunnrPodTotalStatusTask(kunnrPodTotal);
        }

        @Override
        protected void onPostExecute(String result) {
            if (StringUtils.isNotEmpty(result)) {
                try {
                    creatKunnrPodFile();
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("=================>>系统POD总单状态更新完成确认完成！");
                Toast.makeText(getApplicationContext(), "系统POD总单状态更新完成确认完成！" + result, Toast.LENGTH_SHORT).show();
            }
        }

    }

    /**
     * Description: 创建pod照片存放列表
     * author: xg.chen
     * time: 2017/11/29
     * version: 1.0
     */
    private void creatKunnrPodFile() {
        try {
            try {
                new creatKunnrPodFileTask().execute(Tknum.getText().toString());
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Description: 异步创建pod照片存放列表
     * author: xg.chen
     * time: 2017/11/29
     * version: 1.0
     */
    private class creatKunnrPodFileTask extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String param = params[0];
            return DataProviderFactory.getProvider().creatKunnrPodFileTask(param);
        }

        @Override
        protected void onPostExecute(String result) {
            if (StringUtils.isNotEmpty(result)) {
                System.out.println("=================>>系统POD照片路径创建完成确认完成！");
                Toast.makeText(getApplicationContext(), "系统POD照片路径创建完成确认完成！" + result, Toast.LENGTH_SHORT).show();
                try {
                    System.out.println("=================>>返回首页！");
                    Intent intent = new Intent(KunnrPodDetailActivity.this, KunnrActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                    //XPPApplication.exit(KunnrPodDetailActivity.this);
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Description: EXP字典中获取异常信息
     * author: xg.chen
     * time: 2017/11/23
     * version: 1.0
     */
    private class getWarringInfoTask extends AsyncTask<String, Integer, List<BaseDictionary>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showWaitingDialog();
        }

        @Override
        protected List<BaseDictionary> doInBackground(String... params) {
            return DataProviderFactory.getProvider().getWarring();
        }

        @Override
        protected void onPostExecute(List<BaseDictionary> result) {
            dismissWaitingDialog();
            if (result.size() != 0) {
                baseDictionaryList = result;
                initWaringInfoAdpter(baseDictionaryList);
            }
        }
    }

    /**
     * Description: 加载异常信息显示控件
     * author: xg.chen
     * time: 2017/11/23
     * version: 1.0
     */
    public void initWaringInfoAdpter(List<BaseDictionary> listDic) {
        overdialog = null;
        View overdiaView1 = View.inflate(KunnrPodDetailActivity.this,
                R.layout.dialog_search_msg, null);
        overdialog = new Dialog(KunnrPodDetailActivity.this,
                R.style.dialog_xw);
        ListView werksList = (ListView) overdiaView1
                .findViewById(R.id.werksList);
        List<String> list = new ArrayList<>();
        for (BaseDictionary b : listDic) {
            list.add(b.getItemName());
        }
        SettingAdapter settingAdapter = new SettingAdapter(
                getApplicationContext(), list);
        werksList.setAdapter(settingAdapter);
        overdialog.setContentView(overdiaView1);
        overdialog.setCanceledOnTouchOutside(true);
        Button overcancel = (Button) overdiaView1
                .findViewById(R.id.dialog_cancel_btn);
        overcancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                overdialog.cancel();
            }
        });
        overdialog.show();
    }

    /**
     * Description: 异常信息选择器
     * author: xg.chen
     * time: 2017/11/23
     * version: 1.0
     */
    protected class ViewHodler {
        TextView stringList = null;
    }

    protected void resetViewHolder(ViewHodler pViewHolder) {
        pViewHolder.stringList.setText(null);
    }

    public class SettingAdapter extends BaseAdapter {
        private List<String> data = new ArrayList<>();
        private LayoutInflater layoutInflater;

        public SettingAdapter(Context context, List<String> data) {
            this.data = data;
            this.layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHodler hodler;
            if (convertView == null) {
                hodler = new ViewHodler();
                convertView = layoutInflater.inflate(
                        R.layout.dialog_search_list_child, null);
                hodler.stringList = (TextView) convertView
                        .findViewById(R.id.werksName);
                convertView.setTag(hodler);
            } else {
                hodler = (ViewHodler) convertView.getTag();
                resetViewHolder(hodler);
            }
            hodler.stringList.setText(data.get(position));
            // 绑定数据、以及事件触发
            final int n = position;
            convertView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
                    warring.setText(data.get(n));
                    overdialog.cancel();
                }
            });
            return convertView;
        }
    }

    /**
     * Description: 加载时间控件
     * author: xg.chen
     * time: 2017/11/23
     * version: 1.0
     */
    private void selectDatePicker() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm", Locale.CHINA);
        String now = sdf.format(new Date());
        confirmTime.setText(now.split(" ")[0]);

        confrimTimePicker = new DatePicker(this, new DatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                confirmTime.setText(time.split(" ")[0]);
            }
        }, "2010-01-01 00:00", "2099-01-01 00:00"); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        confrimTimePicker.showSpecificTime(false); // 不显示时和分false
        confrimTimePicker.setIsLoop(true); // 不允许循环滚动*/
    }

    /**
     * description: 加载图片开始
     * author: xg.chen
     * date: 2017/6/26 11:56
     * version: 1.0
     */
    private void showWaitingDialog() {
        if (waitingDialog == null) {

            waitingDialog = new Dialog(this, R.style.TransparentDialog);
            waitingDialog.setContentView(R.layout.login_waiting_dialog);
            DialogInterface.OnShowListener showListener = new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    ImageView img = (ImageView) waitingDialog.findViewById(R.id.loading);
                    ((AnimationDrawable) img.getDrawable()).start();
                }
            };
            DialogInterface.OnCancelListener cancelListener = new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    // updateButtonLook(false);
                }
            };
            waitingDialog.setOnShowListener(showListener);
            waitingDialog.setCanceledOnTouchOutside(false);
            waitingDialog.setOnCancelListener(cancelListener);
            waitingDialog.show();
        }
    }

    /**
     * description: 加载结束
     * author: xg.chen
     * date: 2017/6/26 11:56
     * version: 1.0
     */
    private void dismissWaitingDialog() {
        if (waitingDialog != null) {
            ImageView img = (ImageView) waitingDialog.findViewById(R.id.loading);
            ((AnimationDrawable) img.getDrawable()).stop();

            waitingDialog.dismiss();
            waitingDialog = null;
        }
    }


}
