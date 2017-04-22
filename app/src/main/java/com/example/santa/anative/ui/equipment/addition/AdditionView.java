package com.example.santa.anative.ui.equipment.addition;

import android.support.annotation.StringRes;

/**
 * Created by santa on 27.03.17.
 */

interface AdditionView {

    void showMessage(@StringRes int res);
    void onCreateSuccess();

}
