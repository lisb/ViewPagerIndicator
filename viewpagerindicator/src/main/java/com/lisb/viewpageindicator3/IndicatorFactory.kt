package com.lisb.viewpageindicator3

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

interface IndicatorFactory {

    fun createIndicator(inflater: LayoutInflater, parent: ViewGroup, position: Int): View
    
}