package com.robop.scriptrobotcontroller

/**
 * Created by taiga on 2018/04/26.
 */
class MenuItemModel() {
    var itemId=0
    var itemImage=0
    var itemTitle=""
    var itemSub=""

    constructor(resource:Int,title:String,sub:String) : this() {
        this.itemImage=resource
        this.itemTitle=title
        this.itemSub=sub
    }
}