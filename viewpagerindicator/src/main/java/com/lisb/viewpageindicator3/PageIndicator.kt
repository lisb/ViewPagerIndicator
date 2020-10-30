/*
 * Copyright (C) 2011 The Android Open Source Project
 * Copyright (C) 2012 Jake Wharton
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lisb.viewpageindicator3

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.*
import android.widget.FrameLayout
import android.widget.LinearLayout
import kotlin.math.abs

/**
 * This widget implements the dynamic action bar tab behavior that can change
 * across different configurations or circumstances.
 */
class PageIndicator
@JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null) : FrameLayout(context, attrs) {
    private val mIconsLayout: LinearLayout
    private var mViewPagerDelegate: ViewPagerDelegate? = null
    private var mIndicatorFactory: IndicatorFactory? = null
    private var mSelectedPosition = 0
    private val mTouchSlop: Int
    private var mLastMotionX = -1f
    private var mActivePointerId = INVALID_POINTER
    private var mIsDragging = false
    private val mOnPageSelectedListener = object : OnViewPagerChangeListener {
        override fun onPageSelected(position: Int) {
            setCurrentItem(position)
        }

        override fun onDataSetChanged() {
            updateIndicators()
        }
    }

    init {
        isHorizontalScrollBarEnabled = false
        mIconsLayout = LinearLayout(context)
        addView(
            mIconsLayout,
            LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                Gravity.CENTER
            )
        )
        mTouchSlop = ViewConfiguration.get(context).scaledPagingTouchSlop
    }

    fun setViewPager(
        delegate: ViewPagerDelegate,
        initialPosition: Int = delegate.currentItem,
        indicatorFactory: IndicatorFactory
    ) {
        if (mViewPagerDelegate !== delegate || mIndicatorFactory !== indicatorFactory) {
            mViewPagerDelegate?.removeOnViewPagerChangeListener(mOnPageSelectedListener)
            mViewPagerDelegate = delegate
            mIndicatorFactory = indicatorFactory
            delegate.addOnViewPagerChangeListener(mOnPageSelectedListener)
            updateIndicators()
        }

        if (initialPosition != mSelectedPosition) {
            setCurrentItem(mSelectedPosition)
        }
    }

    private fun updateIndicators() {
        mIconsLayout.removeAllViews()
        val viewPager = requireNotNull(mViewPagerDelegate) { "ViewPager has not been bound." }
        val indicatorFactory = requireNotNull(mIndicatorFactory)
        val inflater = LayoutInflater.from(context)
        for (i in 0 until viewPager.pageCount) {
            val view = indicatorFactory.createIndicator(inflater, mIconsLayout, i)
            mIconsLayout.addView(view)
        }
        if (mSelectedPosition > viewPager.pageCount) {
            mSelectedPosition = viewPager.pageCount - 1
        }
        setCurrentItem(mSelectedPosition)
        requestLayout()
    }

    private fun setCurrentItem(item: Int) {
        val viewPager = requireNotNull(mViewPagerDelegate) { "ViewPager has not been bound." }
        mSelectedPosition = item
        viewPager.currentItem = item
        val tabCount = mIconsLayout.childCount
        for (i in 0 until tabCount) {
            val child = mIconsLayout.getChildAt(i)
            val isSelected = i == item
            child.isSelected = isSelected
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent): Boolean {
        if (super.onTouchEvent(ev)) {
            return true
        }
        val viewPager = mViewPagerDelegate ?: return false
        if (viewPager.pageCount == 0) return false

        when (val action = ev.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                mActivePointerId = ev.getPointerId(0)
                mLastMotionX = ev.x
            }
            MotionEvent.ACTION_MOVE -> {
                val activePointerIndex = ev.findPointerIndex(mActivePointerId)
                val x = ev.getX(activePointerIndex)
                val deltaX = x - mLastMotionX
                if (!mIsDragging) {
                    if (abs(deltaX) > mTouchSlop) {
                        mIsDragging = true
                    }
                }
                if (mIsDragging) {
                    mLastMotionX = x
                    if (viewPager.isFakeDragging || viewPager.beginFakeDrag()) {
                        viewPager.fakeDragBy(deltaX)
                    }
                }
            }
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                if (!mIsDragging) {
                    if (mSelectedPosition > 0 && ev.x < mIconsLayout.left) {
                        if (action != MotionEvent.ACTION_CANCEL) {
                            viewPager.currentItem = mSelectedPosition - 1
                        }
                        return true
                    } else if (mSelectedPosition < viewPager.pageCount - 1 && ev.x > mIconsLayout.right) {
                        if (action != MotionEvent.ACTION_CANCEL) {
                            viewPager.currentItem = mSelectedPosition + 1
                        }
                        return true
                    } else {
                        // If indicator is tapped, jump to it's page.
                        if (action != MotionEvent.ACTION_CANCEL) {
                            val x = ev.x - mIconsLayout.left
                            var i = 0
                            val size = mIconsLayout.childCount
                            while (i < size) {
                                val child = mIconsLayout.getChildAt(i)
                                if (x >= child.left && x < child.right) {
                                    viewPager.currentItem = i
                                    break
                                }
                                i++
                            }
                        }
                    }
                }
                mIsDragging = false
                mActivePointerId = INVALID_POINTER
                if (viewPager.isFakeDragging) viewPager.endFakeDrag()
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                val index = ev.actionIndex
                mLastMotionX = ev.getX(index)
                mActivePointerId = ev.getPointerId(index)
            }
            MotionEvent.ACTION_POINTER_UP -> {
                val pointerIndex = ev.actionIndex
                val pointerId = ev.getPointerId(pointerIndex)
                if (pointerId == mActivePointerId) {
                    val newPointerIndex = if (pointerIndex == 0) 1 else 0
                    mActivePointerId = ev.getPointerId(newPointerIndex)
                }
                mLastMotionX = ev.getX(ev.findPointerIndex(mActivePointerId))
            }
        }
        return true
    }

    companion object {
        private const val INVALID_POINTER = -1
    }

}