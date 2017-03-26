package com.example.santa.anative.ui.auth;

import android.support.annotation.StringRes;

import com.example.santa.anative.ui.abstarct.View;

/**
 * Created by santa on 15.03.17.
 */

public interface AuthView extends View {
    void showError(@StringRes int id);
    void showDialog();
    void hideDialog();
    void onStartMain();
    void showAuthorizationForm();
}
