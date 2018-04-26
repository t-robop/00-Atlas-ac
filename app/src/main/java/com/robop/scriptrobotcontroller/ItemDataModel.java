package com.robop.scriptrobotcontroller;

import android.app.Activity;
import android.content.res.Resources;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class ItemDataModel {

    private List<Integer> speedData = new ArrayList<>();
    private List<Integer> timeData = new ArrayList<>();
    private List<String> imageId = new ArrayList<>();

    ItemDataModel(){

    }

    public int getSpeed(int position){
        return speedData.get(position);
    }
    public int getTime(int position){
        return timeData.get(position);
    }
    public String getImageId(int position) {
        return imageId.get(position);
    }

    public void setSpeed(int position, int speed){
        speedData.set(position, speed);
    }
    public  void setTime(int position, int time){
        timeData.set(position, time);
    }
    public void setImageId(int position, String id){
        imageId.set(position, id);
    }

    public void addSpeed(int speed){
        speedData.add(speed);
    }
    public void addTime(int time){
        timeData.add(time);
    }
    public void addImageId(String id){
        imageId.add(id);
    }

    public int listSize(){
        if(imageId.size() == speedData.size() && imageId.size() == timeData.size()) {
            return imageId.size();
        }else {
            //個数が合わない
            return -1;
        }
    }

    public void remove(int position){
        speedData.remove(position);
        timeData.remove(position);
        imageId.remove(position);
    }

}
