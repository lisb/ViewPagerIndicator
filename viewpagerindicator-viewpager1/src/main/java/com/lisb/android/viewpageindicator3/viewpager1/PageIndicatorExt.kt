package com.lisb.android.viewpageindicator3.viewpager1

import androidx.viewpager.widget.ViewPager
import com.lisb.viewpageindicator3.IndicatorFactory
import com.lisb.viewpageindicator3.PageIndicator

fun PageIndicator.setViewPager(
    viewPager: ViewPager,
    initialPosition: Int = viewPager.currentItem,
    indicatorFactory: IndicatorFactory
) {
    setViewPager(ViewPagerDelegate1(viewPager), initialPosition, indicatorFactory)
}