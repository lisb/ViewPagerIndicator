package com.lisb.viewpageindicator3.sample

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.widget.ViewPager2
import com.lisb.android.viewpageindicator3.viewpager2.setViewPager
import com.lisb.viewpageindicator3.PageIndicator
import com.lisb.viewpageindicator3.ResourceIconIndicatorFactory
import java.util.*

class SampleIconsDefault2 : FragmentActivity() {

    private lateinit var mAdapter: TestFragmentAdapter2
    private lateinit var mPager: ViewPager2
    private lateinit var mIndicator: PageIndicator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.simple_icons2)
        mAdapter = TestFragmentAdapter2(this)
        mPager = findViewById(R.id.pager)
        mPager.adapter = mAdapter
        mIndicator = findViewById(R.id.indicator)
        mIndicator.setViewPager(mPager,
            indicatorFactory = object : ResourceIconIndicatorFactory() {
                override fun getIconResId(position: Int): Int {
                    return mAdapter.getIconResId(position)
                }
            })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.random -> {
                val page = RANDOM.nextInt(mAdapter.itemCount)
                Toast.makeText(this, "Changing to page $page", Toast.LENGTH_SHORT).show()
                mPager.currentItem = page
                return true
            }
            R.id.add_page -> {
                if (mAdapter.itemCount < 10) {
                    mAdapter.itemCount = mAdapter.itemCount + 1
                }
                return true
            }
            R.id.remove_page -> {
                if (mAdapter.itemCount > 1) {
                    mAdapter.itemCount = mAdapter.itemCount - 1
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private val RANDOM = Random()
    }
}