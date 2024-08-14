package com.hui.tallybook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hui.tallybook.R;
import com.hui.tallybook.db.ChartItemBean;
import com.hui.tallybook.utils.FloatUtils;

import java.util.List;

public class ChartItemAdapter extends BaseAdapter {
    Context context;
    List<ChartItemBean>mDatas;
    LayoutInflater inflater;

    public ChartItemAdapter(Context context, List<ChartItemBean> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder =null;
        if(convertView == null){
            convertView =  inflater.inflate(R.layout.item_chartfrag_lv, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        //获取显示内容
        ChartItemBean chartItemBean = mDatas.get(position);
        holder.iv.setImageResource(chartItemBean.getsImages());
        holder.typeTv.setText(chartItemBean.getType());
        float ratio = chartItemBean.getRatio();
        String s = FloatUtils.ratioToPercent(ratio);
        holder.ratioTv.setText(s);

        holder.totalTv.setText("￥"+chartItemBean.getTotalMoney());
        return convertView;
    }

    class ViewHolder{
        TextView typeTv, ratioTv, totalTv;
        ImageView iv;
        public ViewHolder(View view){
            typeTv = view.findViewById(R.id.item_chartfrag_tv_type);
            ratioTv = view.findViewById(R.id.item_chartfrag_tv_pert);
            totalTv = view.findViewById(R.id.item_chartfrag_tv_sum);
            iv = view.findViewById(R.id.item_chartfrag_iv);
        }
    }
}
