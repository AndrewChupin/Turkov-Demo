package com.example.santa.anative.view.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.santa.anative.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AboutActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_title) TextView mTitleAbout;
    @BindView(R.id.toolbar_about) Toolbar mToolbarAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        initializeToolbar();
    }

    private void initializeToolbar() {
        mTitleAbout.setText(R.string.title_about);
        mToolbarAbout.setTitle("");
        setSupportActionBar(mToolbarAbout);
        mToolbarAbout.inflateMenu(R.menu.edit_equipment);
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
