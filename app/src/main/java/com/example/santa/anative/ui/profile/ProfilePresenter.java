package com.example.santa.anative.ui.profile;

import com.example.santa.anative.model.entity.Profile;
import com.example.santa.anative.model.repository.ProfileRepository;
import com.example.santa.anative.network.service.ResetService;
import com.example.santa.anative.ui.abstarct.Presentable;
import com.example.santa.anative.ui.reset.ResetView;
import com.example.santa.anative.util.algorithm.RealmSecure;

/**
 * Created by santa on 26.03.17.
 */

public class ProfilePresenter implements Presentable {


    private ProfileView mProfileView;
    private Profile mProfile;

    ProfilePresenter(ProfileView profileView) {
        mProfileView = profileView;
    }

    public void onCreate() {
        ProfileRepository profileRepository = new ProfileRepository(RealmSecure.getDefault());
        mProfile = profileRepository.getProfile();
    }

    public void onSaveProfile(String name, String surname, String paronymic, String email, String phone) {

    }



}
