package com.robop.scriptrobotcontroller;

import android.app.Activity;
import android.content.res.Resources;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class ItemDataList{

    private List<Integer> speedData = new ArrayList<>();
    private List<Integer> timeData = new ArrayList<>();
    private List<String> imageId = new ArrayList<>();

    ItemDataList(){

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

    public void setSpeeed(int position, int speed){
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
    public void change(int myPos, int changePos){

        int tempI;
        String tempS;

        tempI = speedData.get(myPos);
        speedData.set(myPos, speedData.get(changePos));
        speedData.set(changePos, tempI);

        tempI = timeData.get(myPos);
        timeData.set(myPos, timeData.get(changePos));
        timeData.set(changePos, tempI);

        tempS = imageId.get(myPos);
        imageId.set(myPos, imageId.get(changePos));
        imageId.set(changePos, tempS);
    }

    //interruptPosの上に割り込み
    public void interrupt(int myPos, int interruptPos){
        for(int i = myPos; i > interruptPos; i--)
            change(i, i - 1);
        for(int i = myPos; i < interruptPos - 1; i++)
            change(i, i + 1);
    }



}
