package com.lisb.android.viewpageindicator3.viewpager1

import android.database.DataSetObserver
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.lisb.viewpageindicator3.OnViewPagerChangeListener
import com.lisb.viewpageindicator3.ViewPagerDelegate

class ViewPagerDelegate1(private val viewPager: ViewPager) : ViewPagerDelegate {

    private val mOnViewPagerChangeListeners = ArrayList<OnViewPagerChangeListener>()
    private val mListener = Listener()

    override var currentItem: Int
        get() = viewPager.currentItem
        set(value) {
            viewPager.currentItem = value
        }

    override val pageCount: Int
        get() = viewPager.adapter?.count ?: 0

    override val isFakeDragging: Boolean
        get() = viewPager.isFakeDragging

    override fun beginFakeDrag(): Boolean = viewPager.beginFakeDrag()

    override fun endFakeDrag() = viewPager.endFakeDrag()

    override fun fakeDragBy(offsetPxFloat: Float) = viewPager.fakeDragBy(offsetPxFloat)

    override fun addOnViewPagerChangeListener(l: OnViewPagerChangeListener) {
        if (mOnViewPagerChangeListeners.isEmpty()) {
            viewPager.addOnPageChangeListener(mListener)
            viewPager.addOnAdapterChangeListener(mListener)
            viewPager.adapter?.registerDataSetObserver(mListener)
        }
        mOnViewPagerChangeListeners.add(l)
    }

    override fun removeOnViewPagerChangeListener(l: OnViewPagerChangeListener) {
        mOnViewPagerChangeListeners.remove(l)
        if (mOnViewPagerChangeListeners.isEmpty()) {
            viewPager.removeOnPageChangeListener(mListener)
            viewPager.removeOnAdapterChangeListener(mListener)
            viewPager.adapter?.unregisterDataSetObserver(mListener)
        }
    }

    private inner class Listener : ViewPager.OnPageChangeListener,
        ViewPager.OnAdapterChangeListener,
        DataSetObserver() {

        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
        }

        override fun onPageSelected(position: Int) {
            mOnViewPagerChangeListeners.toTypedArray().forEach {
                it.onPageSelected(position)
            }
        }

        override fun onPageScrollStateChanged(state: Int) {
        }

        override fun onAdapterChanged(
            viewPager: ViewPager,
            oldAdapter: PagerAdapter?,
            newAdapter: PagerAdapter?
        ) {
            oldAdapter?.unregisterDataSetObserver(this)
            newAdapter?.registerDataSetObserver(this)
            mOnViewPagerChangeListeners.toTypedArray().forEach {
                it.onDataSetChanged()
            }
        }

        override fun onInvalidated() {
            mOnViewPagerChangeListeners.toTypedArray().forEach {
                it.onDataSetChanged()
            }
        }

        override fun onChanged() {
            mOnViewPagerChangeListeners.toTypedArray().forEach {
                it.onDataSetChanged()
            }
        }
    }

}