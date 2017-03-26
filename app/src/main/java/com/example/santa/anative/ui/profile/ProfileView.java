package com.example.santa.anative.ui.profile;

import android.support.annotation.StringRes;

/**
 * Created by santa on 26.03.17.
 */

public interface ProfileView {
    void showDialog();
    void hideDialog();
    void showError(@StringRes int res);
}
