package com.robop.scriptrobotcontroller;

import java.io.Serializable;

import io.realm.RealmObject;

public class ItemDataModel extends RealmObject implements Serializable {

    // globalConfとseekBarの倍率を掛けた値
    private int rightRelativeSpeed;
    private int leftRelativeSpeed;
    private float seekBarRate = 0.5f;
    private int time;
    private int orderId;
    private int blockState;  //0 : 前後左右移動ブロック, 1 : ループスタートブロック, 2 : ループエンドブロック
    private int loopCount;

    public ItemDataModel() {
    }

    //基本動作ブロックのコンストラクタ
    ItemDataModel(int orderId, int rightRelativeSpeed, int leftRelativeSpeed, int time, int blockState, float seekRate) {
        setOrderId(orderId);
        setRightRelativeSpeed(rightRelativeSpeed);
        setLeftRelativeSpeed(leftRelativeSpeed);
        setTime(time);
        setBlockState(blockState);
        setSeekBarRate(seekRate);
    }

    //ループブロックのコンストラクタ
    ItemDataModel(int orderId, int blockState, int loopCount) {
        setOrderId(orderId);
        setBlockState(blockState);
        setLoopCount(loopCount);
    }

    public int getRightRelativeSpeed() {
        return rightRelativeSpeed;
    }

    public int getLeftRelativeSpeed() {
        return leftRelativeSpeed;
    }

    public float getSeekBarRate() {
        return seekBarRate;
    }

    public int getTime() {
        return time;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getBlockState() {
        return blockState;
    }

    public int getLoopCount() {
        return loopCount;
    }

    void setRightRelativeSpeed(int rightRelativeSpeed) {
        if (rightRelativeSpeed > 100) {
            this.rightRelativeSpeed = 100;
        } else if (rightRelativeSpeed < 0) {
            this.rightRelativeSpeed = 0;
        }
        this.rightRelativeSpeed = rightRelativeSpeed;
    }

    void setLeftRelativeSpeed(int leftRelativeSpeed) {
        if (leftRelativeSpeed > 100) {
            this.leftRelativeSpeed = 100;
        } else if (leftRelativeSpeed < 0) {
            this.leftRelativeSpeed = 0;
        }
        this.leftRelativeSpeed = leftRelativeSpeed;
    }

    public void setSeekBarRate(float seekBarRate) {
        if (seekBarRate > 1) {
            this.seekBarRate = 1;
        } else if (seekBarRate < 0) {
            this.seekBarRate = 0;
        }
        this.seekBarRate = seekBarRate;
    }

    void setTime(int time) {
        if (time < 1) {
            this.time = 1;
        }
        this.time = time;
    }

    void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    void setBlockState(int blockState) {
        if (blockState < 0) {
            this.blockState = 0;
        }
        this.blockState = blockState;
    }

    void setLoopCount(int loopCount) {
        if (loopCount < 0) {
            this.loopCount = 0;
        }
        this.loopCount = loopCount;
    }


}
