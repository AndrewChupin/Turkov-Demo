package com.example.santa.anative.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.santa.anative.R;

import javax.crypto.KeyGenerator;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_title) TextView mTitleProfile;
    @BindView(R.id.toolbar_profile) Toolbar mToolbarProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        initializeToolbar();
    }

    private void initializeToolbar() {
        mTitleProfile.setText(R.string.title_profile);
        mToolbarProfile.setTitle("");
        setSupportActionBar(mToolbarProfile);
        mToolbarProfile.inflateMenu(R.menu.edit_equipment);
        if (getSupportActionBar() != null) getSupportActionBar()
                .setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
