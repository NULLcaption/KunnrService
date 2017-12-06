package com.cxg.kunnr.kunnr.activity.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.cxg.kunnr.kunnr.R;
import com.cxg.kunnr.kunnr.activity.application.XPPApplication;
import com.cxg.kunnr.kunnr.activity.provider.DataProviderFactory;
import com.cxg.kunnr.kunnr.activity.provider.UpdateTask;
import com.cxg.kunnr.kunnr.activity.query.OrmHelper;
import com.cxg.kunnr.kunnr.activity.service.UploadDataService;
import com.cxg.kunnr.kunnr.activity.utils.VersionUpdate;

/**
 * Description: 登录首页
 * author: xg.chen
 * time: 2017/11/20
 * version: 1.0
 */
public class LoginActivity extends AppCompatActivity {

    private EditText et_loginName, et_password;
    private Dialog waitingDialog;
    private LoginTask loginTask;
    private CheckBox cb_remberpsd;
    private int isupdate = 0;// 是否检查升级，0为检查

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        OrmHelper.createInstance(getApplicationContext());
        DataProviderFactory.setContext(getApplicationContext());
        init();
    }

    /**
     * Description: 初始化数据
     * author: xg.chen
     * time: 2017/11/20
     * version: 1.0
     */
    public void init() {
        findViewById(R.id.btn_login).setOnClickListener(BtnClicked);
        et_loginName = (EditText) findViewById(R.id.userName);
        et_password = (EditText) findViewById(R.id.password);
        cb_remberpsd = (CheckBox) findViewById(R.id.remberpsd);
    }

    /**
     * Description: 记住密码显示给当前用户
     * author: xg.chen
     * time: 2017/12/5
     * version: 1.0
     */
    protected void onStart() {
        super.onStart();

        String localpassword = DataProviderFactory.getLocalPassword();
        String loginName = DataProviderFactory.getLoginName();
        String chkpsd = DataProviderFactory.getRemberpsd();
        if (loginName != null && et_loginName != null) {
            et_loginName.setText(DataProviderFactory.getLoginName());
        }
        if ("Y".equals(chkpsd) && localpassword != null && et_password != null) {
            cb_remberpsd.setChecked(true);
            et_password.setText(DataProviderFactory.getLocalPassword());
        }
        et_password.setInputType(InputType.TYPE_CLASS_TEXT
                | InputType.TYPE_TEXT_VARIATION_PASSWORD);

    }

    /**
     * Description: 登录按钮
     * author: xg.chen
     * time: 2017/11/20
     * version: 1.0
     */
    private View.OnClickListener BtnClicked = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_login:
                    tryLogin();
                    break;
            }
        }
    };

    /**
     * Description: 提交登录
     * author: xg.chen
     * time: 2017/11/20
     * version: 1.0
     */
    private void tryLogin() {
        String userName = et_loginName.getText().toString();
        String passWord = et_password.getText().toString();

        if (userName.length() != 0 && passWord.length() != 0) {
            loginTask = new LoginTask();
            loginTask.execute();//执行一个异步任务，需要我们在代码中调用此方法，触发异步任务的执行。
        } else {
            Toast.makeText(getApplicationContext(), "账号或密码不能为空",
                    Toast.LENGTH_SHORT).show();
            return;
        }
    }

    /**
     * Description: 登录
     * author: xg.chen
     * time: 2017/11/20
     * version: 1.0
     */
    private class LoginTask extends AsyncTask<Object, Integer, Integer> {
        //onPreExecute()在execute(Params... params)被调用后立即执行，一般用来在执行后台任务前对UI做一些标记。
        //这里是获取是否记住密码，并设置参数
        protected void onPreExecute() {
            if (cb_remberpsd.isChecked()) {
                DataProviderFactory.setRemberpsd("Y");
            } else {
                DataProviderFactory.setRemberpsd("N");
            }
            DataProviderFactory.setLoginName(et_loginName.getText().toString());
            showWaitingDialog();
        }

        //doInBackground(Object... arg0)在onPreExecute()完成后立即执行，用于执行较为费时的操作，此方法将接收输入参数和返回计算结果。
        //这里去后台验证登录密码
        protected Integer doInBackground(Object... arg0) {
            return DataProviderFactory.getProvider().login(et_password.getText().toString());
        }

        //当后台操作结束时，此方法将会被调用，计算结果将做为参数传递到此方法中，直接将结果显示到UI组件上。
        //这里根据返回的结果集来更新UI组件的value
        protected void onPostExecute(Integer result) {
            switch (result) {
                //登录成功-->首页
                case XPPApplication.SUCCESS:
                    Intent i = new Intent(LoginActivity.this, KunnrActivity.class);
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.login_success), Toast.LENGTH_LONG)
                            .show();
                    Intent iUpload = new Intent(LoginActivity.this,
                            UploadDataService.class);
                    startService(iUpload);
                    finish();
                    new UpdateTask(LoginActivity.this, false).execute();
                    DataProviderFactory.setMenuId(0);
                    startActivity(i);
                    overridePendingTransition(R.anim.in_from_top,
                            R.anim.out_to_bottom);
                    break;
                case XPPApplication.ERR_PASSWORD:
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.login_error_password),
                            Toast.LENGTH_SHORT).show();
                    break;
                case XPPApplication.NO_USER:
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.login_no_user), Toast.LENGTH_SHORT)
                            .show();
                    break;
                case XPPApplication.ERR_ROLE:
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.login_error_role),
                            Toast.LENGTH_SHORT).show();
                    break;
                case XPPApplication.OFFLINE_ERROR_PASSWORD:
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.login_offline_error_password),
                            Toast.LENGTH_SHORT).show();
                    break;
                case XPPApplication.OFFLINE_LOADED:
                    Intent offlineIntent = new Intent(LoginActivity.this,
                            KunnrActivity.class);
                    Intent offlineService = new Intent(LoginActivity.this,
                            UploadDataService.class);
                    startService(offlineService);
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.login_offline_loaded),
                            Toast.LENGTH_SHORT).show();
                    finish();
                    DataProviderFactory.setMenuId(0);
                    offlineIntent.putExtra("menu", "Y");
                    startActivity(offlineIntent);
                    overridePendingTransition(R.anim.in_from_top,
                            R.anim.out_to_bottom);
                    break;
                case XPPApplication.FAIL:
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.login_fail), Toast.LENGTH_SHORT)
                            .show();
                    break;

                case XPPApplication.FAIL_CONNECT_SERVER:
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.login_fail_connect),
                            Toast.LENGTH_SHORT).show();
                    break;
                case XPPApplication.NO_NETWORK:
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.login_no_network),
                            Toast.LENGTH_SHORT).show();
                    break;
                case XPPApplication.NO_MOBILE:
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.login_no_mobile), Toast.LENGTH_SHORT)
                            .show();
                    break;
                case XPPApplication.NOTBUSINESSPHONE:
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.login_not_business),
                            Toast.LENGTH_SHORT).show();
                    break;
                case XPPApplication.UPDATE_VERSION:
                    if (isupdate == 0) {
                        SharedPreferences settings = getSharedPreferences(
                                "PrefsFile", Context.MODE_PRIVATE);
                        new VersionUpdate(LoginActivity.this, settings.getString(
                                "version", ""));
                    } else {
                        Intent i1 = new Intent(LoginActivity.this,
                                KunnrActivity.class);
                        Toast.makeText(getApplicationContext(),
                                getString(R.string.login_success),
                                Toast.LENGTH_SHORT).show();
                        Intent iUpload1 = new Intent(LoginActivity.this,
                                UploadDataService.class);
                        startService(iUpload1);
                        finish();
                        new UpdateTask(LoginActivity.this, false).execute();
                        DataProviderFactory.setMenuId(0);
                        startActivity(i1);
                        overridePendingTransition(R.anim.in_from_top,
                                R.anim.out_to_bottom);

                    }
                    break;
            }
            dismissWaitingDialog();
        }
    }

    /**
     * Description:取消登录
     * author: xg.chen
     * time: 2017/11/20
     * version: 1.0
     */
    private void cancelLogin() {
        dismissWaitingDialog();
        if (loginTask != null) {
            loginTask.cancel(true);
            loginTask = null;
            return;
        }
    }

    /**
     * Description: 获取登录版本
     * author: xg.chen
     * time: 2017/11/20
     * version: 1.0
     */
    private String getVersionName() {
        PackageInfo packInfo = null;
        try {
            PackageManager packageManager = getPackageManager();
            packInfo = packageManager.getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packInfo.versionName;
    }

    /**
     * Description: 显示加载
     * author: xg.chen
     * time: 2017/11/20
     * version: 1.0
     */
    private void showWaitingDialog() {
        if (waitingDialog == null) {
            waitingDialog = new Dialog(this, R.style.TransparentDialog);
            waitingDialog.setContentView(R.layout.login_waiting_dialog);
            DialogInterface.OnShowListener showListener = new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    ImageView img = (ImageView) waitingDialog
                            .findViewById(R.id.loading);
                    ((AnimationDrawable) img.getDrawable()).start();
                }
            };
            DialogInterface.OnCancelListener cancelListener = new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    cancelLogin();
                }
            };
            waitingDialog.setOnShowListener(showListener);
            waitingDialog.setCanceledOnTouchOutside(false);
            waitingDialog.setOnCancelListener(cancelListener);
            waitingDialog.show();
        }
    }

    /**
     * Description: 隐藏加载
     * author: xg.chen
     * time: 2017/11/20
     * version: 1.0
     */
    private void dismissWaitingDialog() {
        if (waitingDialog != null) {
            ImageView img = (ImageView) waitingDialog
                    .findViewById(R.id.loading);
            ((AnimationDrawable) img.getDrawable()).stop();

            waitingDialog.dismiss();
            waitingDialog = null;
        }
    }

}