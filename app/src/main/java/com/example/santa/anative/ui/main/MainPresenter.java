package com.example.santa.anative.ui.main;

import com.example.santa.anative.model.repository.EquipmentRepository;
import com.example.santa.anative.model.repository.PackageRepository;
import com.example.santa.anative.model.repository.ProfileRepository;
import com.example.santa.anative.ui.abstarct.Presentable;
import com.example.santa.anative.util.algorithm.RealmSecure;

/**
 * Created by santa on 26.03.17.
 */

class MainPresenter implements Presentable {

    private MainView mMainView;
    private EquipmentRepository mEquipmentRepository;
    private PackageRepository mPackageRepository;

    MainPresenter(MainView mainView) {
        mMainView = mainView;
    }

    public void onCreate() {

    }
}
