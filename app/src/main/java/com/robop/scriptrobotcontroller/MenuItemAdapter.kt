package com.robop.scriptrobotcontroller

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import java.util.*

/**
 * Created by taiga on 2018/04/26.
 */
class MenuItemAdapter(context: Context) :BaseAdapter() {
    private var mInflater: LayoutInflater?=null
    private var mItemList:ArrayList<MenuItemModel>?=null

    init {
        //レイアウトxmlから、viewを生成
        mInflater = context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        mItemList= ArrayList()
    }

    internal class ViewHolder(view: View) {
        var imgV: ImageView = view.findViewById(R.id.directionImage)
        var titleV: TextView = view.findViewById(R.id.text_title)
        var subV: TextView = view.findViewById(R.id.text_sub)

    }

    fun add(item:MenuItemModel){
        this.mItemList?.add(item)
    }

    fun set(item: MenuItemModel,pos:Int){
        this.mItemList?.set(pos,item)
    }

    override fun getCount(): Int {
        return mItemList!!.size
    }

    override fun getItem(position: Int): MenuItemModel {
        return mItemList!![position]
    }

    override fun getItemId(position: Int): Long {
        //idを返す
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup): View {
        var convertView = convertView

        val holder: ViewHolder
        if (convertView == null) {
            convertView = mInflater!!.inflate(R.layout.menu_list_item, null)
            holder = ViewHolder(convertView)
            convertView!!.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }
        holder.imgV.setImageResource(mItemList!![position].itemImage)
        holder.titleV.text = mItemList!![position].itemTitle
        holder.subV.text=mItemList!![position].itemSub

        return convertView
    }
}