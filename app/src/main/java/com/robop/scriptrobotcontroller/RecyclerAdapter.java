package com.robop.scriptrobotcontroller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{
    private OnRecyclerListener recyclerListener;

    private ArrayList<ItemDataModel> ItemDataArray;

    static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;
        TextView speedRight;
        TextView speedLeft;
        TextView time;
        ImageView image;

        ViewHolder(View view){
            super(view);
            this.linearLayout = view.findViewById(R.id.item_frame);
            this.speedRight = view.findViewById(R.id.text_speed_right);
            this.speedLeft = view.findViewById(R.id.text_speed_left);
            this.time = view.findViewById(R.id.text_time);
            this.image = view.findViewById(R.id.direction_item_image);
        }
    }

    RecyclerAdapter(ArrayList<ItemDataModel> itemDataList, OnRecyclerListener listener){
        ItemDataArray = itemDataList;
        recyclerListener = listener;
    }

    public ItemDataModel getItem(int position) {
        return ItemDataArray.get(position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.speedRight.setText("右パワー : " + ItemDataArray.get(position).getRightSpeed());
        holder.speedLeft.setText("左パワー : " + ItemDataArray.get(position).getLeftSpeed());
        holder.time.setText(ItemDataArray.get(position).getTime() + "秒");

        holder.linearLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                recyclerListener.onRecyclerClicked(view, position);
            }
        });
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

    public interface OnRecyclerListener {
        void onRecyclerClicked(View view, int position);
    }
}
