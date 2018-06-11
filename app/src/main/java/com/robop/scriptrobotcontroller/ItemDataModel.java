package com.robop.scriptrobotcontroller;

import java.io.Serializable;

import io.realm.RealmObject;

public class ItemDataModel extends RealmObject implements Serializable {

    private int rightSpeed;
    private int leftSpeed;
    private int time;
    private int orderId;
    private int blockState;
    private int loopCount;

    public ItemDataModel(){}

    //基本動作ブロックのコンストラクタ
    ItemDataModel(int orderId, int rightSpeed, int leftSpeed, int time, int blockState){
        setOrderId(orderId);
        setRightSpeed(rightSpeed);
        setLeftSpeed(leftSpeed);
        setTime(time);
        setBlockState(blockState);
    }

    //ループブロックのコンストラクタ
    ItemDataModel(int orderId, int blockState, int loopCount){
        setOrderId(orderId);
        setBlockState(blockState);
        setLoopCount(loopCount);
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
    public int getBlockState(){
        return blockState;
    }
    public int getLoopCount(){
        return loopCount;
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
    void setBlockState(int blockState){
        if(blockState < 0){
            this.blockState = 0;
        }
        this.blockState = blockState;
    }
    void setLoopCount(int loopCount){
        if(loopCount < 0){
            this.loopCount = 0;
        }
        this.loopCount = loopCount;
    }


}
