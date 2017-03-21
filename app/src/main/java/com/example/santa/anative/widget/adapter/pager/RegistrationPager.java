package com.example.santa.anative.widget.adapter.pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.santa.anative.ui.registration.CodeFragment;
import com.example.santa.anative.ui.registration.RegistrationDataFragment;

/**
 * Created by santa on 04.03.17.
 */

public class RegistrationPager extends FragmentPagerAdapter {

    public static final int DATA_FRAGMENT = 0;
    public static final int CODE_FRAGMENT = 1;


    public RegistrationPager(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new RegistrationDataFragment();
            case 1:
                return new CodeFragment();
            default:
                break;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
