package com.example.santa.anative.ui.equipment.edit;

import android.support.annotation.StringRes;

import com.example.santa.anative.model.entity.Equipment;
import com.example.santa.anative.model.entity.Profile;

/**
 * Created by santa on 31.03.17.
 */

interface EditEquipmentView {

    void showMessage(@StringRes int res);
    void onEditSuccess();
    void showDialog();
    void hideDialog();

}
