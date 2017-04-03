package com.example.santa.anative.ui.equipment.schedule;

import android.support.annotation.StringRes;

/**
 * Created by santa on 31.03.17.
 */

public interface ScheduleView {

    void showMessage(@StringRes int res);
    void showDialog();
    void hideDialog();

}
