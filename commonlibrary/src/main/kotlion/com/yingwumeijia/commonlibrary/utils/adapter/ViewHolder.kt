package com.yingwumeijia.commonlibrary.utils.adapter

import android.content.Context
import android.support.annotation.IdRes
import android.support.annotation.LayoutRes
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

/**
 * Created by jamisonline on 2017/5/25.
 */
class ViewHolder constructor(context: Context, parent: ViewGroup, @LayoutRes layoutId: Int, position: Int) {

    var mViews: SparseArray<View>
    var mContentView: View
    var mContent: Context
    var mPosition: Int
    var mLayoutId: Int

    init {
        this.mViews = SparseArray()
        this.mContent = context
        this.mLayoutId = layoutId
        this.mPosition = position
        this.mContentView = LayoutInflater.from(mContent).inflate(layoutId, parent, false)
        this.mContentView.setTag(this)

    }


    companion object {
        fun get(context: Context, convertView: View, parent: ViewGroup, @LayoutRes layoutId: Int, position: Int): ViewHolder {
            if (convertView == null)
                return ViewHolder(context, parent, layoutId, position)
            else
                return convertView.getTag() as ViewHolder
        }
    }

    /**
     * get view from viewId
     */
    fun getView(@IdRes viewId: Int): View {
        var view = mViews[viewId]
        if (view == null) {
            view = mContentView.findViewById(viewId)
            mViews.put(viewId, view)
        }
        return view
    }


    fun setText(@IdRes viewId: Int, text: String) {
        var tv = getView(viewId) as TextView
        tv.text = text
    }

    fun setImgRes(@IdRes id: Int, @IdRes resId: Int) {
        var iv = getView(id) as ImageView
        iv.setImageResource(resId)
    }


}