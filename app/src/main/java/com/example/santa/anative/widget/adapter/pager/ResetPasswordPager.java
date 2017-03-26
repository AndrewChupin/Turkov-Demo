package com.example.santa.anative.widget.adapter.pager;

/**
 * Created by santa on 04.03.17.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.santa.anative.ui.registration.RegistrationCodeFragment;
import com.example.santa.anative.ui.reset.EmailFragment;
import com.example.santa.anative.ui.reset.NewPasswordFragment;

/**
 * Created by santa on 04.03.17.
 */

public class ResetPasswordPager extends FragmentPagerAdapter {

    public static final int EMAIL_FRAGMENT = 0;
    public static final int CODE_FRAGMENT = 1;
    public static final int PASSWORD_FRAGMENT = 2;


    public ResetPasswordPager(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new EmailFragment();
            case 1:
                return new RegistrationCodeFragment();
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

