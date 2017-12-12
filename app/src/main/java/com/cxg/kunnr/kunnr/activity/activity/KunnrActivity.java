package com.cxg.kunnr.kunnr.activity.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cxg.kunnr.kunnr.R;
import com.cxg.kunnr.kunnr.activity.adapter.KunnrAdapter;
import com.cxg.kunnr.kunnr.activity.application.XPPApplication;
import com.cxg.kunnr.kunnr.activity.provider.DataProviderFactory;
import com.cxg.kunnr.kunnr.activity.query.KunnrPodTotal;
import com.cxg.kunnr.kunnr.activity.query.UserInfo;
import com.cxg.kunnr.kunnr.activity.query.ZsshipmentInTransit;
import com.cxg.kunnr.kunnr.activity.query.ZsshipmentInTransitH;
import com.cxg.kunnr.kunnr.activity.utils.ExitApplication;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 经销商POD确认到货首页
 * author: xg.chen
 * time: 2017/11/21
 * version: 1.0
 */
public class KunnrActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView kunnrName, kunnrId;
    private KunnrAdapter kunnrAdapter;
    private List<ZsshipmentInTransitH> zslipsList;
    private List<ZsshipmentInTransitH> zslipsList_001;
    private ListView zslipsListView;
    private Dialog waitingDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kunnr);

        //左侧菜单
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initView();
        initData();

        ExitApplication.getInstance().addActivity(this);
    }

    /**
     * Description: 初始化页面
     * author: xg.chen
     * time: 2017/11/22
     * version: 1.0
     */
    public void initView() {
        kunnrName = (TextView) findViewById(R.id.kunnrName);
        kunnrId = (TextView) findViewById(R.id.kunnrId);
        zslipsListView = (ListView) findViewById(R.id.lv_sysinfo);//详单列表
    }

    /**
     * Description: 初始化数据
     * author: xg.chen
     * time: 2017/11/22
     * version: 1.0
     */
    public void initData() {
        String uname = "经销商";
        String loginname = "";
        try {
            loginname = DataProviderFactory.getLoginName();
            if (loginname != null) {
                uname = UserInfo.findByLoginName(loginname).getUserName();
            }
        } catch (Exception e) {
            Log.i("DataProviderFactory.getLoginName() is null",e.toString());
        }
        String role = DataProviderFactory.getRoleId();
        if (role.equals(XPPApplication.jxscg_role) || role.equals(XPPApplication.jxs_role)) {
            kunnrId.setText(DataProviderFactory.getCouldId());
            //根据经销商编号获取POD信息
            try {
                new getPodInfoTask().execute(kunnrId.getText().toString());
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getApplicationContext(), "非经销商或经销商仓管，无法提报！", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Description: 加载运单信息
     * author: xg.chen
     * time: 2017/11/22
     * version: 1.0
     */
    public void initKunnrAdapterData () {
        if (zslipsList == null) {
            zslipsList_001 = new ArrayList<>();
        } else if (zslipsList.size() != 0) {
            //1、加载运单汇总列表
            kunnrAdapter = new KunnrAdapter(zslipsList, this);
            zslipsListView.setAdapter(kunnrAdapter);

        }
    }

    /**
     * Description: 在EXP中创建获取到的在途运单数据
     * author: xg.chen
     * time: 2017/11/28
     * version: 1.0
     */
    public void creatKunnrPodTotal(List<ZsshipmentInTransitH> list) {
        try {
            if (list.size()!=0) {
                for (ZsshipmentInTransitH zss:list) {
                    KunnrPodTotal kunnrPodTotal = new KunnrPodTotal();
                    kunnrPodTotal.setWaybillId(zss.getTknum());
                    kunnrPodTotal.setKunnr(DataProviderFactory.getCouldId());
                    kunnrPodTotal.setEstimateDate(zss.getYdhrq());
                    kunnrPodTotal.setCreator(DataProviderFactory.getUserId());
                    kunnrPodTotal.setFlag("A");
                    kunnrPodTotal.setType("M");

                    try {
                        new creatKunnrPodTotalTask().execute(kunnrPodTotal);
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            Log.i("creatKunnrPodTotal Error",e.toString());
        }
    }

    /**
     * Description: 异步在EXP中创建获取到的在途运单数据
     * author: xg.chen
     * time: 2017/11/28
     * version: 1.0
     */
    private class creatKunnrPodTotalTask extends AsyncTask<KunnrPodTotal,Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(KunnrPodTotal... params) {
            KunnrPodTotal kunnrPodTotal = params[0];
            return DataProviderFactory.getProvider().creatKunnrPodTotalTask(kunnrPodTotal);
        }
        @Override
        protected void onPostExecute(String result) {
            if (StringUtils.isNotEmpty(result)) {
                if (result.equals("SUCCESS")) {
                    //添加数据成功以后显示数据列表
                    try {
                        initKunnrAdapterData();
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "保存明细数据数据出错！", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    /**
     * Description: 根据登陆经销商获取在途运单汇总信息
     * author: xg.chen
     * time: 2017/11/22
     * version: 1.0
     */
    private class getPodInfoTask extends AsyncTask<String , Integer, List<ZsshipmentInTransitH>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showWaitingDialog();
            Toast.makeText(getApplicationContext(), "正在加载数据，请稍后！", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected List<ZsshipmentInTransitH> doInBackground(String... params) {
            String string = params[0];
            return DataProviderFactory.getProvider().getPodInfoTask(string);
        }

        @Override
        protected void onPostExecute(List<ZsshipmentInTransitH> result) {
            dismissWaitingDialog();
            System.out.println("+++>>result+"+result);
            if (result.size() != 0) {
                zslipsList = result;
                String userName = zslipsList.get(0).getName1();
                kunnrName.setText(userName + getString(R.string.home_hi));
                //在EXP中创建获取到的在途运单数据

                creatKunnrPodTotal(zslipsList);
            }
        }


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.kunnr, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_gallery) {//POD确认到货

        } else if (id == R.id.nav_manage) {//退出
            ExitApplication.getInstance().exit();
            Toast.makeText(getApplicationContext(), "退出应用", Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
