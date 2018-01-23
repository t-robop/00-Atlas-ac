package com.robop.scriptrobotcontroller;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by user on 2017/12/20.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private ItemDataList item;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView speed;
        TextView time;
        ImageView image;
        public ViewHolder(View view) {
            super(view);
            this.speed = (TextView)view.findViewById(R.id.text1);
            this.time = (TextView)view.findViewById(R.id.text2);
            this.image  = (ImageView)view.findViewById(R.id.directionImage);
        }
    }
    
    public RecyclerAdapter(ItemDataList itemDataList) {
        item = itemDataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.speed.setText("" + item.getSpeed(position));
        holder.time.setText("" + item.getTime(position));
        holder.image.setImageResource(R.drawable.edit_icon);
    }

    @Override
    public int getItemCount() {
        return item.listSize();
    }
}