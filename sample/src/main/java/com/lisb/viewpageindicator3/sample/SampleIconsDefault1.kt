package com.lisb.viewpageindicator3.sample

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.viewpager.widget.ViewPager
import com.lisb.android.viewpageindicator3.viewpager1.ViewPagerDelegate1
import com.lisb.viewpageindicator3.PageIndicator
import com.lisb.viewpageindicator3.ResourceIconIndicatorFactory
import java.util.*

class SampleIconsDefault1 : FragmentActivity() {

    private lateinit var mAdapter: TestFragmentAdapter1
    private lateinit var mPager: ViewPager
    private lateinit var mIndicator: PageIndicator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.simple_icons1)
        mAdapter = TestFragmentAdapter1(supportFragmentManager)
        mPager = findViewById(R.id.pager)
        mPager.adapter = mAdapter
        mIndicator = findViewById(R.id.indicator)
        mIndicator.setViewPager(delegate = ViewPagerDelegate1(mPager),
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
                val page = RANDOM.nextInt(mAdapter.count)
                Toast.makeText(this, "Changing to page $page", Toast.LENGTH_SHORT).show()
                mPager.currentItem = page
                return true
            }
            R.id.add_page -> {
                if (mAdapter.count < 10) {
                    mAdapter.count = mAdapter.count + 1
                }
                return true
            }
            R.id.remove_page -> {
                if (mAdapter.count > 1) {
                    mAdapter.count = mAdapter.count - 1
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