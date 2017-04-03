package com.example.santa.anative.ui.profile;

import com.example.santa.anative.model.entity.Profile;
import com.example.santa.anative.model.repository.ProfileRepository;
import com.example.santa.anative.ui.common.Presentable;
import com.example.santa.anative.util.realm.RealmSecure;

import io.realm.Realm;

/**
 * Created by santa on 26.03.17.
 */

public class ProfilePresenter implements Presentable {

    private ProfileView mProfileView;
    private Profile mProfile;
    private Realm mRealm;

    ProfilePresenter(ProfileView profileView) {
        mProfileView = profileView;
    }


    @Override
    public void onCreate() {
        mRealm = RealmSecure.getDefault();
        mProfile = ProfileRepository.getProfile(mRealm);
        mProfileView.showProfileInfo(mProfile);
    }


    @Override
    public void onDestroy() {
        mRealm.close();
    }


    void onSaveProfile(String name, String surname, String paronymic, String email, String phone) {
        mProfile.setName(name);
        mProfile.setSurname(surname);
        mProfile.setPatronymic(paronymic);
        mProfile.setEmail(email);
        mProfile.setPhone(phone);
        ProfileRepository.updateProfile(mRealm, mProfile);
    }

}
