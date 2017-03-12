package com.example.santa.anative.ui.viewpager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by santa on 04.03.17.
 */

public class NonSwappableViewPager extends ViewPager {

    public NonSwappableViewPager(Context context) {
        super(context);
    }

    public NonSwappableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }
}
