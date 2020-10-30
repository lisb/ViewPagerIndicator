package com.lisb.android.viewpageindicator3.viewpager2

import androidx.viewpager2.widget.ViewPager2
import com.lisb.viewpageindicator3.IndicatorFactory
import com.lisb.viewpageindicator3.PageIndicator

fun PageIndicator.setViewPager(
    viewPager: ViewPager2,
    initialPosition: Int = viewPager.currentItem,
    indicatorFactory: IndicatorFactory
) {
    setViewPager(ViewPagerDelegate2(viewPager), initialPosition, indicatorFactory)
}