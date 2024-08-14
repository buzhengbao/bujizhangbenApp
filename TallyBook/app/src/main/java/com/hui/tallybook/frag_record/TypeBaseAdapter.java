package com.hui.tallybook.frag_record;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hui.tallybook.R;
import com.hui.tallybook.db.TypeBean;

import java.util.List;

public class TypeBaseAdapter extends BaseAdapter {
    Context context;
    List<TypeBean> mDatas;
    int selectPos = 0;//选中位置
    public TypeBaseAdapter(Context context, List<TypeBean> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
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
    //此适配器不考虑复用问题，因为所有的Item都显示在界面上，不会因为滑动消失，没有剩余的conversation，所以不用复用

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_recordfrag_gv, parent, false);
        //查找布局中的控件
        ImageView image = convertView.findViewById(R.id.item_recordfrag_gv_iv);
        TextView text = convertView.findViewById(R.id.item_recordfrag_gv_tv_title);
        //获取指定位置的数据源
        TypeBean typeBean = mDatas.get(position);
        text.setText(typeBean.getTypename());
        //判断当前位置是否为选择位置，如果是选中位置，就设置为带颜色的图片，否则会灰色图片
        if (selectPos == position) {
            image.setImageResource(typeBean.getSimageId());
        } else {
            image.setImageResource(typeBean.getImageId());
        }
        return convertView;
    }
}
