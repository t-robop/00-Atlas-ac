package com.robop.scriptrobotcontroller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by user on 2017/12/11.
 */

public class ListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private ItemDataList item;

    ListAdapter(Context context, ItemDataList itemDataList){

        //レイアウトxmlから、viewを生成
        mInflater = (LayoutInflater)context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        item = itemDataList;
    }

    @Override
    public int getCount() {
        return item.listSize();
    }

    @Override
    public Object getItem(int position) {
        return item.getImageId(position);
    }

    @Override
    public long getItemId(int position) {
        //idを返す
        return position;
    }

    static class ViewHolder{
        TextView speed;
        TextView time;
        ImageView image;

        ViewHolder(View view){
            this.speed = (TextView)view.findViewById(R.id.text1);
            this.time = (TextView)view.findViewById(R.id.text2);
            this.image  = (ImageView)view.findViewById(R.id.directionImage);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        ViewHolder holder;
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.list_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.speed.setText("" + item.getSpeed(position));
        holder.time.setText("" + item.getTime(position));
        holder.image.setImageResource(R.drawable.edit_icon);

        return convertView;
    }
}
