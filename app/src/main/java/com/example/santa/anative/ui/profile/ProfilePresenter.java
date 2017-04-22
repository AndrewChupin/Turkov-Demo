package com.example.santa.anative.ui.profile;

import com.example.santa.anative.R;
import com.example.santa.anative.model.entity.Package;
import com.example.santa.anative.model.entity.Profile;
import com.example.santa.anative.model.pack.ProfilePackage;
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
 * Created by santa on 26.03.17.
 */

class ProfilePresenter implements Presenter {

    private ProfileView mProfileView;
    private Realm mRealm;
    private MainService mainService;

    ProfilePresenter(ProfileView profileView) {
        mProfileView = profileView;
    }


    @Override
    public void onCreate() {
        mRealm = RealmSecure.getDefault();
    }


    Profile findProfile() {
        return ProfileRepository.getProfile(mRealm);
    }


    @Override
    public void onDestroy() {
        mRealm.close();
        if (mainService != null) mainService.onStop();
    }


    void onSaveProfile(final Profile profile) {
        Package pack = ProfilePackage.createEditProfilePackage(profile);

        Connection connection = new Connection(HOST, PORT);
        mainService = new MainService(connection, pack);
        mainService.onSubscribe(new Observer() {
            @Override
            public void onError(int code) {
                mProfileView.showMessage(R.string.unknown_error);
                mainService.onStop();
            }

            @Override
            public void onSuccess() {
                mProfileView.showMessage(R.string.data_saved_success);
                ProfileRepository.updateProfile(mRealm, profile);
            }
        });
        mainService.execute();
    }

}
