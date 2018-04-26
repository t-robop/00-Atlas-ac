package com.robop.scriptrobotcontroller;

public class ItemDataModel {

    private int rightSpeed;
    private int leftSpeed;
    private int time;
    private int id;

    ItemDataModel(int id, int rightSpeed, int leftSpeed, int time){
        setImageId(id);
        setRightSpeed(rightSpeed);
        setLeftSpeed(leftSpeed);
        setTime(time);
    }
    public int getRightSpeed(){
        return rightSpeed;
    }
    public int getLeftSpeed(){
        return leftSpeed;
    }
    public int getTime(){
        return time;
    }
    public int getImageId() {
        return id;
    }

    private void setRightSpeed(int rightSpeed){
        this.rightSpeed = rightSpeed;
    }
    private void setLeftSpeed(int leftSpeed){
        this.leftSpeed = leftSpeed;
    }
    private void setTime(int time){
        this.time = time;
    }
    private void setImageId(int id){
        this.id = id ;
    }


}
