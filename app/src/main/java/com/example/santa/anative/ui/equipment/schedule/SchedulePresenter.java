package com.example.santa.anative.ui.equipment.schedule;

import com.example.santa.anative.R;
import com.example.santa.anative.model.entity.Equipment;
import com.example.santa.anative.model.entity.Package;
import com.example.santa.anative.model.entity.Setting;
import com.example.santa.anative.model.pack.EquipmentPackage;
import com.example.santa.anative.model.pack.SettingPackage;
import com.example.santa.anative.model.repository.EquipmentRepository;
import com.example.santa.anative.model.repository.ProfileRepository;
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

public class SchedulePresenter implements Presenter {

    private ScheduleView mScheduleView;
    private Realm mRealm;
    private int senderId;

    SchedulePresenter(ScheduleView scheduleView) {
        mScheduleView = scheduleView;
    }

    @Override
    public void onCreate() {
        mRealm = RealmSecure.getDefault();
        senderId = ProfileRepository.getProfile(mRealm).getClientId();
    }


    Equipment onFindEquipment(int id) {
        return EquipmentRepository.getEquipmentsById(mRealm, id);
    }


    void onSendAllSettingsStatus(Equipment equipment) {
        Package pack = EquipmentPackage.createAllStateSettingsPackage(senderId, equipment);

        Connection connection = new Connection(HOST, PORT);
        final MainService mainService = new MainService(connection, pack);
        mainService.onSubscribe(new Observer() {
            @Override
            public void onError(int code) {
                mScheduleView.showMessage(R.string.unknown_error);
                mainService.onStop();
            }

            @Override
            public void onSuccess() {
                mScheduleView.showMessage(R.string.data_saved_success);
            }
        });
        mainService.execute();
    }

    void onSendSettingStatus(int equipmentId, Setting setting) {
        Package pack = SettingPackage.createStateSettingPackage(senderId, equipmentId, setting);

        Connection connection = new Connection(HOST, PORT);
        final MainService mainService = new MainService(connection, pack);
        mainService.onSubscribe(new Observer() {
            @Override
            public void onError(int code) {
                mScheduleView.showMessage(R.string.unknown_error);
                mainService.onStop();
            }

            @Override
            public void onSuccess() {
                mScheduleView.showMessage(R.string.data_saved_success);
                // TODO SAVE RESULT
            }
        });
        mainService.execute();
    }


    @Override
    public void onDestroy() {
        mRealm.close();
    }


}