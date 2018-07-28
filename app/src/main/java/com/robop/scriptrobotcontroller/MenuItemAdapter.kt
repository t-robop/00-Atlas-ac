package com.robop.scriptrobotcontroller

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import java.util.*

class MenuItemAdapter(context: Context) : BaseAdapter() {
    private var mInflater: LayoutInflater? = null
    private var mItemList: ArrayList<MenuItemModel>? = null

    init {
        //レイアウトxmlから、viewを生成
        mInflater = context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        mItemList = ArrayList()
    }

    internal class ViewHolder(view: View) {
        var imgBack: ImageView = view.findViewById(R.id.menu_item_bg)
        var imgV: ImageView = view.findViewById(R.id.direction_image)
        var titleV: TextView = view.findViewById(R.id.text_title)
        var subV: TextView = view.findViewById(R.id.text_sub)

    }

    fun add(item: MenuItemModel) {
        this.mItemList!!.add(item)
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
        var view = convertView

        val holder: ViewHolder
        if (view == null) {
            view = mInflater!!.inflate(R.layout.menu_list_item, null)
            holder = ViewHolder(view)
            view!!.tag = holder
        } else {
            holder = view.tag as ViewHolder
        }
        holder.imgV.setImageResource(mItemList!![position].itemImage)
        holder.titleV.text = mItemList!![position].itemTitle
        holder.subV.text = mItemList!![position].itemSub

        if (position == 4 || position == 5) {
            holder.imgBack.setImageResource(R.drawable.back_loop);
        } else {
            holder.imgBack.setImageResource(R.drawable.back_move);
        }

        return view
    }
}