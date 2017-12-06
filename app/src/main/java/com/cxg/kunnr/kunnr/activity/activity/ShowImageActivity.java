package com.cxg.kunnr.kunnr.activity.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.cxg.kunnr.kunnr.R;
import com.cxg.kunnr.kunnr.activity.utils.ExitApplication;
import com.cxg.kunnr.kunnr.activity.utils.PictureShowUtils;
import com.polites.android.GestureImageView;

/**
 * Description: 照片展示
 * author: xg.chen
 * time: 2017/11/27
 * version: 1.0
 */

public class ShowImageActivity extends AppCompatActivity {

    private GestureImageView image ;
    private String dir ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_show_image);
        Bundle bun =getIntent().getExtras();
        if(bun!=null){
            dir=(String) bun.get("dir");
        }
        init();
        PictureShowUtils.getImage(dir);
        image.setImageBitmap(PictureShowUtils.getImage(dir));

        ExitApplication.getInstance().addActivity(this);
    }

    public void  init(){
        image=(GestureImageView) findViewById(R.id.image);
    }
}
