package com.cxg.kunnr.kunnr.activity.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cxg.kunnr.kunnr.R;
import com.cxg.kunnr.kunnr.activity.activity.KunnrPodDetailActivity;
import com.cxg.kunnr.kunnr.activity.query.ZsshipmentInTransitH;

import java.util.ArrayList;
import java.util.List;
/**
 * Description: 在运单汇总列表
 * author: xg.chen
 * time: 2017/11/22
 * version: 1.0
 */
public class KunnrAdapter extends BaseAdapter {

    public List<ZsshipmentInTransitH> zslipsList = new ArrayList<>();
    private LayoutInflater layoutInflater;
    private Activity activity;

    private LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT);

    public KunnrAdapter(List<ZsshipmentInTransitH> parameterList,Activity activity) {
        params.leftMargin = 2;
        this.zslipsList = parameterList;
        this.layoutInflater = LayoutInflater.from(activity);
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return zslipsList.size();
    }

    @Override
    public Object getItem(int position) {
        return zslipsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        /*列表字段赋值*/
        ViewHolder hodler;
        if (convertView == null) {
            hodler = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.layout_kunnr_pod, null);
            hodler.Tknum = (TextView) convertView.findViewById(R.id.Tknum);//运单号
            hodler.Lfimg1 = (TextView) convertView.findViewById(R.id.Lfimg1);//成品
            hodler.Lfimg2 = (TextView) convertView.findViewById(R.id.Lfimg2);//辅销品
            hodler.Ydhrq = (TextView) convertView.findViewById(R.id.Ydhrq);//应到货日期
            hodler.Name1 = (TextView) convertView.findViewById(R.id.Name1);//收货地址
            convertView.setTag(hodler);
        } else {
            hodler = (ViewHolder) convertView.getTag();
            resetViewHolder(hodler);
        }
        ZsshipmentInTransitH zsshipmentInTransitH = zslipsList.get(position);
        hodler.Tknum.setText(zsshipmentInTransitH.getTknum());
        hodler.Lfimg1.setText(zsshipmentInTransitH.getLfimg1());
        hodler.Lfimg2.setText(zsshipmentInTransitH.getLfimg2());
        hodler.Ydhrq.setText(zsshipmentInTransitH.getYdhrq());
        hodler.Name1.setText(zsshipmentInTransitH.getName1());

        /*设置列表的点击事件*/
        final int n = position;
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, KunnrPodDetailActivity.class);
                intent.putExtra("zsshipmentInTransitH",zslipsList.get(n));
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
            }
        });

        return convertView;
    }

    private void resetViewHolder(ViewHolder pViewHodler) {
        pViewHodler.Tknum.setText(null);
        pViewHodler.Lfimg1.setText(null);
        pViewHodler.Lfimg2.setText(null);
        pViewHodler.Ydhrq.setText(null);
        pViewHodler.Name1.setText(null);

    }

    private static class ViewHolder {
        TextView Tknum = null;
        TextView Lfimg1 = null;
        TextView Lfimg2 = null;
        TextView Ydhrq = null;
        TextView Name1 = null;
    }
}
