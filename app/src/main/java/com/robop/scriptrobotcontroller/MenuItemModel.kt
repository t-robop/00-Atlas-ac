package com.robop.scriptrobotcontroller

class MenuItemModel() {
    var itemImage = 0
    var itemTitle = ""
    var itemSub = ""

    constructor(resource: Int, title: String, sub: String) : this() {
        this.itemImage = resource
        this.itemTitle = title
        this.itemSub = sub
    }
}