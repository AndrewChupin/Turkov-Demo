package com.example.santa.anative.ui.equipment.detail;

import com.example.santa.anative.model.entity.Equipment;

/**
 * Created by santa on 04.04.17.
 */

public interface EquipmentDetail {
    void onBindData(Equipment equipment);
    void changeStatus();
}
