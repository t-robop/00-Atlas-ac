package com.robop.scriptrobotcontroller;

import android.app.Activity;
import android.content.res.Resources;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2017/12/18.
 */

public class ItemDataList extends Activity{

    private List<Integer> speedData = new ArrayList<Integer>();
    private List<Integer> timeData = new ArrayList<Integer>();
    private List<String> imageId = new ArrayList<String>();

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



}
