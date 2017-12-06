package com.cxg.kunnr.kunnr.activity.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.widget.ImageButton;

/**
 * Description: 图片触发控件
 * author: xg.chen
 * time: 2017/11/27
 * version: 1.0
 */
@SuppressLint("DrawAllocation")
public class MyImageButton extends ImageButton {
    private String text = null;
    private int color;
    private int height ;
    public MyImageButton(Context context) {
        super(context);
    }
    public void setText(String text){
        this.text = text;
    }
    public void setColor(int color){
        this.color = color;
    }
    public void setHeight(int height) {
        this.height = height;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint=new Paint();
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setColor(color);
        canvas.drawText(text, 15, 140, paint);
    }
}
