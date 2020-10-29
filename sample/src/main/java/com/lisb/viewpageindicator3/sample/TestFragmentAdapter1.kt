package com.lisb.viewpageindicator3.sample

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class TestFragmentAdapter1(fm: FragmentManager) : FragmentPagerAdapter(
    fm,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) {
    private var mCount = CONTENT.size
    override fun getItem(position: Int): Fragment {
        return TestFragment.newInstance(CONTENT[position % CONTENT.size])
    }

    override fun getCount(): Int {
        return mCount
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return CONTENT[position % CONTENT.size]
    }

    fun getIconResId(index: Int): Int {
        return ICONS[index % ICONS.size]
    }

    fun setCount(count: Int) {
        if (count in 1..10) {
            mCount = count
            notifyDataSetChanged()
        }
    }

    companion object {
        val CONTENT = arrayOf("This", "Is", "A", "Test")
        val ICONS = intArrayOf(
            R.drawable.perm_group_calendar,
            R.drawable.perm_group_camera,
            R.drawable.perm_group_device_alarms,
            R.drawable.perm_group_location
        )
    }
}