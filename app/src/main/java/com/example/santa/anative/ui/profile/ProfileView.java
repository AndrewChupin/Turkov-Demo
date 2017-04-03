package com.example.santa.anative.ui.profile;

import android.support.annotation.StringRes;

import com.example.santa.anative.model.entity.Profile;

/**
 * Created by santa on 26.03.17.
 */

public interface ProfileView {

    void showProfileInfo(Profile profile);
    void showDialog();
    void hideDialog();
    void showMessage(@StringRes int res);
}
