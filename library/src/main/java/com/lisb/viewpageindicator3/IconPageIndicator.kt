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
import android.view.Gravity
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import kotlin.math.abs

/**
 * This widget implements the dynamic action bar tab behavior that can change
 * across different configurations or circumstances.
 */
class IconPageIndicator
@JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null) : FrameLayout(context, attrs),
    PageIndicator {
    private val mIconsLayout: LinearLayout
    private var mViewPager: ViewPager? = null
    private var mListener: OnPageChangeListener? = null
    private var mSelectedIndex = 0
    private val mTouchSlop: Int
    private var mLastMotionX = -1f
    private var mActivePointerId = INVALID_POINTER
    private var mIsDragging = false

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

    override fun onPageScrollStateChanged(arg0: Int) {
        mListener?.onPageScrollStateChanged(arg0)
    }

    override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {
        mListener?.onPageScrolled(arg0, arg1, arg2)
    }

    override fun onPageSelected(arg0: Int) {
        setCurrentItem(arg0)
        mListener?.onPageSelected(arg0)
    }

    override fun setViewPager(view: ViewPager) {
        if (mViewPager === view) return
        mViewPager?.removeOnPageChangeListener(this)
        if (view.adapter == null) throw IllegalStateException("ViewPager does not have adapter instance.")
        mViewPager = view
        view.addOnPageChangeListener(this)
        notifyDataSetChanged()
    }

    override fun notifyDataSetChanged() {
        mIconsLayout.removeAllViews()
        val viewPager = requireNotNull(mViewPager) { "ViewPager has not been bound." }
        val adapter =
            requireNotNull(viewPager.adapter) { "ViewPager does not have adapter instance." }
        val iconAdapter = adapter as IconPagerAdapter
        val count = iconAdapter.getCount()
        for (i in 0 until count) {
            val view = ImageView(context, null, R.attr.vpiIconPageIndicatorStyle3)
            view.setImageResource(iconAdapter.getIconResId(i))
            mIconsLayout.addView(view)
        }
        if (mSelectedIndex > count) {
            mSelectedIndex = count - 1
        }
        setCurrentItem(mSelectedIndex)
        requestLayout()
    }

    override fun setViewPager(view: ViewPager, initialPosition: Int) {
        setViewPager(view)
        setCurrentItem(initialPosition)
    }

    override fun setCurrentItem(item: Int) {
        val viewPager = requireNotNull(mViewPager) { "ViewPager has not been bound." }
        mSelectedIndex = item
        viewPager.currentItem = item
        val tabCount = mIconsLayout.childCount
        for (i in 0 until tabCount) {
            val child = mIconsLayout.getChildAt(i)
            val isSelected = i == item
            child.isSelected = isSelected
        }
    }

    override fun setOnPageChangeListener(listener: OnPageChangeListener?) {
        mListener = listener
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent): Boolean {
        if (super.onTouchEvent(ev)) {
            return true
        }
        val viewPager = mViewPager ?: return false
        val adapter =
            requireNotNull(viewPager.adapter) { "ViewPager does not have adapter instance." }
        if (adapter.count == 0) return false

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
                    val count = adapter.count
                    if (mSelectedIndex > 0 && ev.x < mIconsLayout.left) {
                        if (action != MotionEvent.ACTION_CANCEL) {
                            viewPager.currentItem = mSelectedIndex - 1
                        }
                        return true
                    } else if (mSelectedIndex < count - 1 && ev.x > mIconsLayout.right) {
                        if (action != MotionEvent.ACTION_CANCEL) {
                            viewPager.currentItem = mSelectedIndex + 1
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