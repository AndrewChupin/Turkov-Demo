package com.example.santa.anative.ui.equipment.setting;

import com.example.santa.anative.model.entity.Equipment;
import com.example.santa.anative.model.entity.Setting;

/**
 * Created by santa on 04.04.17.
 */

public interface EquipmentSetting {
    void onBindData(Setting setting);
    Setting onSaveData(Setting setting);
}
