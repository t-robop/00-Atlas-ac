package com.robop.scriptrobotcontroller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    //private ItemDataModel item;
    ArrayList<ItemDataModel> ItemDataArray = new ArrayList<>();


    ListAdapter(Context context, ArrayList itemDataList){

        //レイアウトxmlから、viewを生成
        mInflater = (LayoutInflater)context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        ItemDataArray = itemDataList;
    }

    @Override
    public int getCount() {
        return ItemDataArray.size();
    }

    @Override
    public ItemDataModel getItem(int position) {
        return ItemDataArray.get(position);
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
        //TODO ここ治して！
        holder.speed.setText("パワー : " + ItemDataArray.get(position).getRightSpeed());
        holder.speed.setText("パワー : " + ItemDataArray.get(position).getLeftSpeed());
        holder.time.setText(ItemDataArray.get(position).getTime() + "秒");
        //holder.image.setImageResource(R.drawable.edit_icon);

        switch (ItemDataArray.get(position).getOrderId()){
            case 1:
                holder.image.setImageResource(R.drawable.move_front);
                break;

            case 2:
                holder.image.setImageResource(R.drawable.move_back);
                break;

            case 3:
                holder.image.setImageResource(R.drawable.move_left);
                break;

            case 4:
                holder.image.setImageResource(R.drawable.move_right);
                break;

            default:
                holder.image.setImageResource(R.drawable.edit_icon);
                break;
        }

        return convertView;
    }
}
