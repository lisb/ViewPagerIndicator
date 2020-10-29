package com.lisb.viewpageindicator3.sample

import android.os.Bundle

class SampleIconsDefault : BaseSampleActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.simple_icons)
        mAdapter = TestFragmentAdapter(supportFragmentManager)
        mPager = findViewById(R.id.pager)
        mPager.adapter = mAdapter
        mIndicator = findViewById(R.id.indicator)
        mIndicator.setViewPager(mPager)
    }
}