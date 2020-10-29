package com.lisb.viewpageindicator3

interface ViewPagerDelegate {

    var currentItem: Int

    val pageCount: Int

    val isFakeDragging: Boolean

    fun beginFakeDrag(): Boolean

    fun endFakeDrag()

    fun fakeDragBy(offsetPxFloat: Float)

    fun addOnViewPagerChangeListener(l: OnViewPagerChangeListener)

    fun removeOnViewPagerChangeListener(l: OnViewPagerChangeListener)

}