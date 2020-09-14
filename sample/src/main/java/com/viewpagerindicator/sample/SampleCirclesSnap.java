package com.viewpagerindicator.sample;

import android.os.Bundle;
import com.viewpagerindicator.CirclePageIndicator;

public class SampleCirclesSnap extends BaseSampleActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_circles);

        mAdapter = new TestFragmentAdapter(getSupportFragmentManager());

        mPager = findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);

        CirclePageIndicator indicator = findViewById(R.id.indicator);
        mIndicator = indicator;
        indicator.setViewPager(mPager);
        indicator.setSnap(true);
    }
}