package com.robop.scriptrobotcontroller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{
    private View.OnClickListener listener;

    private ArrayList<ItemDataModel> ItemDataArray;
    static class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout linearLayout;
        TextView speed;     //TODO 画像の項目足りてない！　speedRightとSpeedLeftを用意して！
        TextView time;
        ImageView image;
        ViewHolder(View view){
            super(view);
            this.linearLayout = view.findViewById(R.id.itemFrame);
            this.speed = view.findViewById(R.id.text_speed);
            this.time = view.findViewById(R.id.text_time);
            this.image  = view.findViewById(R.id.directionItemImage);
        }
    }

    RecyclerAdapter(Context context, ArrayList<ItemDataModel> itemDataList){
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

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //TODO 画像の項目足りてない！　speedRightとSpeedLeftを用意して！
        holder.speed.setText("パワー : " + ItemDataArray.get(position).getRightSpeed());
        holder.speed.setText("パワー : " + ItemDataArray.get(position).getLeftSpeed());    //TODO 最終的に左パワーの値が表示されるようになっている
        holder.time.setText(ItemDataArray.get(position).getTime() + "秒");
 //       holder.linearLayout.setId(holder.getAdapterPosition());
        holder.linearLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                listener.onClick(view);
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

    public void setOnItemClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount(){
        return ItemDataArray.size();
    }
}
