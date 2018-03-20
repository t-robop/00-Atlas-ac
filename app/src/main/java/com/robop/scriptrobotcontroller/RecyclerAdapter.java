package com.robop.scriptrobotcontroller;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private ItemDataList item;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView speed;
        TextView time;
        ImageView image;
        public ViewHolder(View view) {
            super(view);
            this.speed = view.findViewById(R.id.text1);
            this.time = view.findViewById(R.id.text2);
            this.image  = view.findViewById(R.id.directionImage);
        }
    }
    
    public RecyclerAdapter(ItemDataList itemDataList) {
        item = itemDataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);

        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.speed.setText("パワー : " + item.getSpeed(position));
        holder.time.setText(item.getTime(position) + "秒");
        holder.image.setImageResource(R.drawable.edit_icon);
    }

    @Override
    public int getItemCount() {
        return item.listSize();
    }
}