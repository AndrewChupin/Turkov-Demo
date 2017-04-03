package com.example.santa.anative.ui.reset;

import android.support.annotation.StringRes;

import com.example.santa.anative.ui.common.View;

/**
 * Created by santa on 22.03.17.
 */

public interface ResetView  extends View {

    void showDialog();
    void hideDialog();
    void showMessage(@StringRes int res);
    void onAuth(String email);
    void onEnterCode();
    void onResetPassword();

}
