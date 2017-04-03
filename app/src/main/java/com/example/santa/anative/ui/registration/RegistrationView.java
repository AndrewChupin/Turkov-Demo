package com.example.santa.anative.ui.registration;

import android.support.annotation.StringRes;

import com.example.santa.anative.ui.common.View;

/**
 * Created by santa on 18.03.17.
 */

public interface RegistrationView extends View {

    void showDialog();
    void hideDialog();
    void showMessage(@StringRes int res);
    void onAuth(String email);
    void onEnterCode();

}
