package com.lisb.android.viewpageindicator3.viewpager2

import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.lisb.viewpageindicator3.OnViewPagerChangeListener
import com.lisb.viewpageindicator3.ViewPagerDelegate

class ViewPagerDelegate2(private val viewPager: ViewPager2) : ViewPagerDelegate {

    private val mOnViewPagerChangeListeners = ArrayList<OnViewPagerChangeListener>()
    private val adapter = requireNotNull(viewPager.adapter) { "ViewPager2 has no adapter" }

    init {
        viewPager.registerOnPageChangeCallback(OnPageChangeCallback())
        adapter.registerAdapterDataObserver(AdapterDataObserver())
    }

    override var currentItem: Int
        get() = viewPager.currentItem
        set(value) {
            viewPager.currentItem = value
        }

    override val pageCount: Int
        get() = viewPager.adapter?.itemCount ?: 0

    override val isFakeDragging: Boolean
        get() = viewPager.isFakeDragging

    override fun beginFakeDrag(): Boolean = viewPager.beginFakeDrag()

    override fun endFakeDrag() {
        viewPager.endFakeDrag()
    }

    override fun fakeDragBy(offsetPxFloat: Float) {
        viewPager.fakeDragBy(offsetPxFloat)
    }

    override fun addOnViewPagerChangeListener(l: OnViewPagerChangeListener) {
        ensureAdapterNotChanged()
        mOnViewPagerChangeListeners.add(l)
    }

    override fun removeOnViewPagerChangeListener(l: OnViewPagerChangeListener) {
        ensureAdapterNotChanged()
        mOnViewPagerChangeListeners.remove(l)
    }

    private fun ensureAdapterNotChanged() {
        require(viewPager.adapter === adapter) { "ViewPager2#adapter is changed" }
    }

    private inner class AdapterDataObserver : RecyclerView.AdapterDataObserver() {
        override fun onChanged() {
            dispatchToOnViewPagerChangeListeners()
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            dispatchToOnViewPagerChangeListeners()
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            dispatchToOnViewPagerChangeListeners()
        }

        private fun dispatchToOnViewPagerChangeListeners() {
            ensureAdapterNotChanged()
            mOnViewPagerChangeListeners.toTypedArray().forEach {
                it.onDataSetChanged()
            }

        }
    }

    private inner class OnPageChangeCallback : ViewPager2.OnPageChangeCallback() {

        override fun onPageSelected(position: Int) {
            ensureAdapterNotChanged()
            mOnViewPagerChangeListeners.toTypedArray().forEach {
                it.onPageSelected(position)
            }
        }

    }

}