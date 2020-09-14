package com.viewpagerindicator.sample;

import android.os.Bundle;

public class SampleTitlesStyledLayout extends BaseSampleActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.themed_titles);

        mAdapter = new TestFragmentAdapter(getSupportFragmentManager());

        mPager = findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);

        mIndicator = findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);
    }
}