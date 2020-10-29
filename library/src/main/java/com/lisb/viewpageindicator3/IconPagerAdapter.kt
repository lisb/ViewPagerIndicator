package com.lisb.viewpageindicator3

interface IconPagerAdapter {
    /**
     * Get icon representing the page at `index` in the adapter.
     */
    fun getIconResId(index: Int): Int

    // From PagerAdapter
    fun getCount(): Int
}