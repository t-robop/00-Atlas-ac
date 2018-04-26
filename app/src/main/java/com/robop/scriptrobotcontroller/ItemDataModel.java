package com.robop.scriptrobotcontroller;

public class ItemDataModel {

    private int rightSpeed;
    private int leftSpeed;
    private int time;
    private int orderId;

    ItemDataModel(int orderId, int rightSpeed, int leftSpeed, int time){
        setOrderId(orderId);
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
    public int getOrderId() {
        return orderId;
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
    private void setOrderId(int orderId){
        this.orderId = orderId ;
    }


}
