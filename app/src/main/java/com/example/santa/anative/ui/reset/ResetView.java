package com.example.santa.anative.ui.reset;

import android.support.annotation.StringRes;

import com.example.santa.anative.ui.abstarct.View;

/**
 * Created by santa on 22.03.17.
 */

public interface ResetView  extends View {

    void showDialog();
    void hideDialog();
    void showError(@StringRes int res);
    void onAuth(String email);
    void onEnterCode();
    void onResetPassword();

}
