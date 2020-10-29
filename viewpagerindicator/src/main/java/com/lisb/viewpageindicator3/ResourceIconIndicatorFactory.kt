package com.lisb.viewpageindicator3

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

abstract class ResourceIconIndicatorFactory : IndicatorFactory {

    override fun createIndicator(inflater: LayoutInflater, parent: ViewGroup, position: Int): View {
        val view = inflater.inflate(R.layout.vpi_resource_icon_indicator, parent, false)
        val imageView = view.findViewById<ImageView>(R.id.vps_icon)
        imageView.setImageResource(getIconResId(position))
        return view
    }

    abstract fun getIconResId(position: Int): Int
}