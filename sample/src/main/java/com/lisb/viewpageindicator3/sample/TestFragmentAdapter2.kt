package com.lisb.viewpageindicator3.sample

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class TestFragmentAdapter2(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    private var mCount = CONTENT.size

    override fun getItemCount(): Int {
        return mCount
    }

    override fun createFragment(position: Int): Fragment {
        return TestFragment.newInstance(CONTENT[position % CONTENT.size])
    }

    fun getIconResId(index: Int): Int {
        return ICONS[index % ICONS.size]
    }

    fun setItemCount(count: Int) {
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