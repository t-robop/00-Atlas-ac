package com.robop.scriptrobotcontroller;

import java.io.Serializable;

public class ItemDataModel implements Serializable {

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

    void setRightSpeed(int rightSpeed){
        if (rightSpeed > 255) {
            this.rightSpeed = 255;
        } else if (rightSpeed < 0) {
            this.rightSpeed = 0;
        }
        this.rightSpeed = rightSpeed;
    }
    void setLeftSpeed(int leftSpeed){
        if (leftSpeed > 255) {
            this.leftSpeed = 255;
        } else if (leftSpeed < 0) {
            this.leftSpeed = 0;
        }
        this.leftSpeed = leftSpeed;
    }
    void setTime(int time){
        if (time < 1) {
            this.time = 1;
        }
        this.time = time;
    }
    void setOrderId(int orderId){
        this.orderId = orderId ;
    }


}
