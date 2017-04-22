package com.example.santa.anative.ui.equipment.setting;

import android.content.Context;

import com.example.santa.anative.model.entity.Equipment;
import com.example.santa.anative.ui.equipment.detail.ConvectorDetailFragment;
import com.example.santa.anative.ui.equipment.detail.DesiccantDetailFragment;
import com.example.santa.anative.ui.equipment.detail.EquipmentDetail;
import com.example.santa.anative.ui.equipment.detail.VentilationDetailFragment;

/**
 * Created by santa on 04.04.17.
 */

class EquipmentSettingFactory {

     static EquipmentSetting getFragmentDetail(int equipmentType) {
         EquipmentSetting detailFragment;

        switch (equipmentType) {
            case Equipment.DESICCANT:
                detailFragment = new DesiccantSettingFragment();
                break;
            case Equipment.CONVECTOR:
            default:
                detailFragment = new VentilationSettingFragment();
                break;
        }

        return detailFragment;
    }

}
