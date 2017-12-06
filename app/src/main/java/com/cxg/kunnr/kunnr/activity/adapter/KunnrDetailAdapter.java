package com.cxg.kunnr.kunnr.activity.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cxg.kunnr.kunnr.R;
import com.cxg.kunnr.kunnr.activity.query.ZsshipmentInTransit;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 在途运单明细列表
 * author: xg.chen
 * time: 2017/11/23
 * version: 1.0
 */

public class KunnrDetailAdapter  extends BaseAdapter {

    public List<ZsshipmentInTransit> zsshipmentInTransitList = new ArrayList<>();
    private LayoutInflater layoutInflater;
    private Activity activity;

    private LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT);

    public KunnrDetailAdapter(List<ZsshipmentInTransit> parameterList, Activity activity) {
        params.leftMargin = 2;
        this.zsshipmentInTransitList = parameterList;
        this.layoutInflater = LayoutInflater.from(activity);
        this.activity = activity;
    }


    @Override
    public int getCount() {
        return zsshipmentInTransitList.size();
    }

    @Override
    public Object getItem(int position) {
        return zsshipmentInTransitList.get(position);
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
            convertView = layoutInflater.inflate(R.layout.layout_kunnr_pod_detail, null);
            hodler.Maktx = (TextView) convertView.findViewById(R.id.Maktx);
            hodler.Lfimg = (TextView) convertView.findViewById(R.id.Lfimg);
            hodler.Vrkme = (TextView) convertView.findViewById(R.id.Vrkme);
            convertView.setTag(hodler);
        } else {
            hodler = (ViewHolder) convertView.getTag();
            resetViewHolder(hodler);
        }

        ZsshipmentInTransit zsshipmentInTransit = zsshipmentInTransitList.get(position);
        hodler.Maktx.setText(zsshipmentInTransit.getMvgr1());
        hodler.Lfimg.setText(zsshipmentInTransit.getLfimg());
        hodler.Vrkme.setText(zsshipmentInTransit.getUnit());

        return convertView;
    }

    private void resetViewHolder(ViewHolder pViewHodler) {
        pViewHodler.Maktx.setText(null);
        pViewHodler.Lfimg.setText(null);
        pViewHodler.Vrkme.setText(null);
    }

    private static class ViewHolder {
        TextView Maktx = null;
        TextView Lfimg = null;
        TextView Vrkme = null;
    }
}
