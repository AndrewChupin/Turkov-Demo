package com.example.santa.anative.widget.adapter.pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.santa.anative.model.entity.Error;
import com.example.santa.anative.ui.equipment.detail.ErrorFragment;

import java.util.ArrayList;

/**
 * Created by santa on 12.03.17.
 */

public class ErrorPager extends FragmentPagerAdapter {

    ArrayList<Error> mErrorsList;

    public ErrorPager(FragmentManager fm, ArrayList<Error> errorsList) {
        super(fm);
        mErrorsList = errorsList;
    }

    public ErrorPager(FragmentManager fm) {
        super(fm);
    }

    @Override
    public float getPageWidth (int position) {
        return 0.93f;
    }

    @Override
    public Fragment getItem(int position) {
        return ErrorFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return mErrorsList.size();
    }
}
