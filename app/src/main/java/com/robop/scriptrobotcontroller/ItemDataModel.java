package com.robop.scriptrobotcontroller;

import java.io.Serializable;

import io.realm.RealmObject;

public class ItemDataModel extends RealmObject implements Serializable {

    // globalConfとseekBarの倍率を掛けた値
    private int rightRerativeSpeed;
    private int leftRerativeSpeed;
    private float seekBarRate = 0.5f;
    private int time;
    private int orderId;
    private int blockState;
    private int loopCount;

    public ItemDataModel(){}

    //基本動作ブロックのコンストラクタ
    ItemDataModel(int orderId, int rightRerativeSpeed, int leftRerativeSpeed, int time, int blockState, float seekRate){
        setOrderId(orderId);
        setRightRerativeSpeed(rightRerativeSpeed);
        setLeftRerativeSpeed(leftRerativeSpeed);
        setTime(time);
        setBlockState(blockState);
        setSeekBarRate(seekRate);
    }

    //ループブロックのコンストラクタ
    ItemDataModel(int orderId, int blockState, int loopCount){
        setOrderId(orderId);
        setBlockState(blockState);
        setLoopCount(loopCount);
    }

    public int getRightRerativeSpeed(){
        return rightRerativeSpeed;
    }
    public int getLeftRerativeSpeed(){
        return leftRerativeSpeed;
    }
    public float getSeekBarRate() {
        return seekBarRate;
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

    void setRightRerativeSpeed(int rightRerativeSpeed){
        if (rightRerativeSpeed > 100) {
            this.rightRerativeSpeed = 100;
        } else if (rightRerativeSpeed < 0) {
            this.rightRerativeSpeed = 0;
        }
        this.rightRerativeSpeed = rightRerativeSpeed;
    }
    void setLeftRerativeSpeed(int leftRerativeSpeed){
        if (leftRerativeSpeed > 100) {
            this.leftRerativeSpeed = 100;
        } else if (leftRerativeSpeed < 0) {
            this.leftRerativeSpeed = 0;
        }
        this.leftRerativeSpeed = leftRerativeSpeed;
    }
    public void setSeekBarRate(float seekBarRate) {
        if (seekBarRate > 1) {
            this.seekBarRate = 1;
        }else if (seekBarRate < 0 ){
            this.seekBarRate = 0;
        }
        this.seekBarRate = seekBarRate;
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
