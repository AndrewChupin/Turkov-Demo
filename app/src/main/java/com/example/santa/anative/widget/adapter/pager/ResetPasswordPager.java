package com.example.santa.anative.widget.adapter.pager;

/**
 * Created by santa on 04.03.17.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.santa.anative.ui.registration.CodeFragment;
import com.example.santa.anative.ui.fragment.EmailFragment;
import com.example.santa.anative.ui.fragment.NewPasswordFragment;

/**
 * Created by santa on 04.03.17.
 */

public class ResetPasswordPager extends FragmentPagerAdapter {


    public ResetPasswordPager(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new EmailFragment();
            case 1:
                return new CodeFragment();
            case 2:
                return new NewPasswordFragment();
            default:
                break;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}

