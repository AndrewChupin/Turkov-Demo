package com.example.santa.anative.ui.equipment.setting;

import com.example.santa.anative.R;
import com.example.santa.anative.model.pack.SettingPackage;
import com.example.santa.anative.model.entity.Package;
import com.example.santa.anative.model.entity.Setting;
import com.example.santa.anative.model.repository.ProfileRepository;
import com.example.santa.anative.model.repository.SettingsRepository;
import com.example.santa.anative.network.common.Observer;
import com.example.santa.anative.network.connection.Connection;
import com.example.santa.anative.network.service.MainService;
import com.example.santa.anative.ui.common.Presenter;
import com.example.santa.anative.util.realm.RealmSecure;

import io.realm.Realm;

import static com.example.santa.anative.application.Configurations.HOST;
import static com.example.santa.anative.application.Configurations.PORT;

/**
 * Created by santa on 31.03.17.
 */

class SettingEquipmentPresenter implements Presenter {

    private SettingEquipmentView mSettingEquipmentView;
    private Realm mRealm;

    SettingEquipmentPresenter(SettingEquipmentView settingEquipmentView) {
        mSettingEquipmentView = settingEquipmentView;
    }


    Setting getSetting(int id) {
        return SettingsRepository.getSettingById(mRealm, id);
    }

    void onSaveSetting(int equipmentId, int equipmentType, Setting setting) {
        int senderId = ProfileRepository.getProfile(mRealm).getClientId();
        Package pack = SettingPackage.createSettingPackage(senderId, equipmentId, equipmentType, setting);

        Connection connection = new Connection(HOST, PORT);
        MainService mainService = new MainService(connection, pack);
        mainService.onSubscribe(new Observer() {
            @Override
            public void onError(int code) {
                mSettingEquipmentView.showMessage(R.string.unknown_error);
            }

            @Override
            public void onSuccess() {
                mSettingEquipmentView.showMessage(R.string.data_saved_success);
            }
        });
        mainService.execute();
    }


    @Override
    public void onCreate() {
        mRealm = RealmSecure.getDefault();
    }

    @Override
    public void onDestroy() {
        mRealm.close();
    }
}
