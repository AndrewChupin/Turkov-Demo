package com.example.santa.anative.ui.equipment.detail;

import android.content.Context;

import com.example.santa.anative.model.entity.Equipment;

/**
 * Created by santa on 04.04.17.
 */

class EquipmentDetailFactory {

     static EquipmentDetail getFragmentDetail(int equipmentType) {
        EquipmentDetail detailFragment;

        switch (equipmentType) {
            case Equipment.DESICCANT:
                detailFragment = new DesiccantDetailFragment();
                break;
            case Equipment.CONVECTOR:
                detailFragment = new ConvectorDetailFragment();
                break;
            default:
                detailFragment = new VentilationDetailFragment();
                break;
        }

        return detailFragment;
    }

}
