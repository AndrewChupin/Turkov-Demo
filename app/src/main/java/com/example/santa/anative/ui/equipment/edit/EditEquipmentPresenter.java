package com.example.santa.anative.ui.equipment.edit;

import com.example.santa.anative.R;
import com.example.santa.anative.model.entity.Equipment;
import com.example.santa.anative.model.repository.EquipmentRepository;
import com.example.santa.anative.network.common.Observer;
import com.example.santa.anative.network.connection.Connection;
import com.example.santa.anative.network.connection.ConnectionManager;
import com.example.santa.anative.network.service.MainService;
import com.example.santa.anative.ui.common.Presentable;
import com.example.santa.anative.util.realm.RealmSecure;

import io.realm.Realm;

import static com.example.santa.anative.application.Configurations.HOST;
import static com.example.santa.anative.application.Configurations.PORT;

/**
 * Created by santa on 31.03.17.
 */

class EditEquipmentPresenter implements Presentable {

    private Realm mRealm;
    private EditEquipmentView mEditEquipmentView;


    EditEquipmentPresenter(EditEquipmentView equipmentView) {
        mEditEquipmentView = equipmentView;
    }


    @Override
    public void onCreate() {
        mRealm = RealmSecure.getDefault();
    }


    void sendEquipment(final Equipment equipment) {
        // TODO CRATE PACKAGE

       /* Connection connection = ConnectionManager.getDefault().create(HOST, PORT);
        MainService mainService = new MainService(connection);
        mainService.onSubscribe(new Observer() {
            @Override
            public void onError(int code) {
                mEditEquipmentView.hideDialog();
                mEditEquipmentView.showMessage(code);
            }

            @Override
            public void onComplete() {
                mEditEquipmentView.hideDialog();
                mEditEquipmentView.onEditSuccess();
                EquipmentRepository.saveEquipment(mRealm, equipment);
            }
        });
        mEditEquipmentView.showDialog();
        mainService.onStart();*/
    }

    Equipment onFindEquipment(int id) {
        return EquipmentRepository.getEquipmentsById(mRealm, id);
    }


    @Override
    public void onDestroy() {
        mRealm.close();
    }

}
