package com.robop.scriptrobotcontroller;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{

    ArrayList<ItemDataModel> ItemDataArray;
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView speed;
        TextView time;
        ImageView image;
        ViewHolder(View view){
            super(view);
            this.speed = view.findViewById(R.id.text1);
            this.time = view.findViewById(R.id.text2);
            this.image  = view.findViewById(R.id.directionImage);
        }
    }

    RecyclerAdapter(Context context, ArrayList itemDataList){
        ItemDataArray = itemDataList;
    }
    
    public ItemDataModel getItem(int position) {
        return ItemDataArray.get(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.speed.setText("パワー : " + ItemDataArray.get(position).getRightSpeed());
        holder.speed.setText("パワー : " + ItemDataArray.get(position).getLeftSpeed());
        holder.time.setText(ItemDataArray.get(position).getTime() + "秒");
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
    }

    @Override
    public int getItemCount(){
        return ItemDataArray.size();
    }
}
