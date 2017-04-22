package com.example.santa.anative.ui.equipment.addition;

import com.example.santa.anative.model.entity.Package;
import com.example.santa.anative.model.pack.EquipmentPackage;
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
 * Created by santa on 27.03.17.
 */

public class AdditionEquipmentPresenter implements Presenter {

    private Realm mRealm;
    private AdditionView mAdditionalView;


    AdditionEquipmentPresenter(AdditionView additionalView) {
        mAdditionalView = additionalView;
    }


    @Override
    public void onCreate() {
        mRealm = RealmSecure.getDefault();
    }


    void onCreateEquipment(String name, byte[] pin, String serial) {
        int senderId = ProfileRepository.getProfile(mRealm).getClientId();
        Package pack = EquipmentPackage.createAdditionEquipmentPackage(senderId, name, pin, serial);

        Connection connection = new Connection(HOST, PORT);
        final MainService mainService = new MainService(connection, pack);
        mainService.onSubscribe(new Observer() {
            @Override
            public void onError(int code) {
                mAdditionalView.showMessage(code);
                mainService.onStop();
            }

            @Override
            public void onSuccess() {
                mAdditionalView.onCreateSuccess();
            }
        });
        mainService.execute();
    }


    @Override
    public void onDestroy() {
        mRealm.close();
    }
}
