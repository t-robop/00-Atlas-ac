package com.robop.scriptrobotcontroller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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
            this.speed = view.findViewById(R.id.text1);
            this.time = view.findViewById(R.id.text2);
            this.image  = view.findViewById(R.id.directionImage);
        }
    }

    @SuppressLint({"InflateParams", "SetTextI18n"})
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
        holder.speed.setText("パワー : " + item.getSpeed(position));
        holder.time.setText(item.getTime(position) + "秒");
        //holder.image.setImageResource(R.drawable.edit_icon);

        switch (item.getImageId(position)){
            case "1":
                holder.image.setImageResource(R.mipmap.forward);
                break;

            case "2":
                holder.image.setImageResource(R.mipmap.backward);
                break;

            case "3":
                holder.image.setImageResource(R.mipmap.left_rotation);
                break;

            case "4":
                holder.image.setImageResource(R.mipmap.right_rotation);
                break;

            default:
                holder.image.setImageResource(R.drawable.edit_icon);
                break;
        }

        return convertView;
    }
}
