package com.example.santa.anative.ui.equipment.addition;

import com.example.santa.anative.network.common.Observer;
import com.example.santa.anative.network.connection.Connection;
import com.example.santa.anative.network.connection.ConnectionManager;
import com.example.santa.anative.network.service.AuthService;
import com.example.santa.anative.network.service.MainService;
import com.example.santa.anative.ui.common.Presentable;
import com.example.santa.anative.util.realm.RealmSecure;

import io.realm.Realm;

import static com.example.santa.anative.application.Configurations.HOST;
import static com.example.santa.anative.application.Configurations.PORT;

/**
 * Created by santa on 27.03.17.
 */

public class AdditionalPresenter implements Presentable {

    private Realm mRealm;
    private AdditionalView mAdditionalView;


    AdditionalPresenter(AdditionalView additionalView) {
        mAdditionalView = additionalView;
    }


    @Override
    public void onCreate() {
        mRealm = RealmSecure.getDefault();
    }


    void onServiceCreateEquipment(String name, byte[] pin, String serial) {
        // TODO CRATE PACKAGE

        Connection connection = ConnectionManager.getDefault().create(HOST, PORT);
        MainService mainService = new MainService(connection);
        mainService.onSubscribe(new Observer() {
            @Override
            public void onError(int code) {
                mAdditionalView.onMessage(code);
            }

            @Override
            public void onComplete() {
                mAdditionalView.onCreateSuccess();
            }
        });
        mainService.onStart();
    }


    @Override
    public void onDestroy() {
        mRealm.close();
    }
}
