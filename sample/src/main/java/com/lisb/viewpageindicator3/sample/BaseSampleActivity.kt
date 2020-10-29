package com.lisb.viewpageindicator3.sample

import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.viewpager.widget.ViewPager
import com.lisb.viewpageindicator3.PageIndicator
import java.util.*

abstract class BaseSampleActivity : FragmentActivity() {
    protected lateinit var mAdapter: TestFragmentAdapter
    protected lateinit var mPager: ViewPager
    protected lateinit var mIndicator: PageIndicator

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
                    mIndicator.notifyDataSetChanged()
                }
                return true
            }
            R.id.remove_page -> {
                if (mAdapter.count > 1) {
                    mAdapter.count = mAdapter.count - 1
                    mIndicator.notifyDataSetChanged()
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