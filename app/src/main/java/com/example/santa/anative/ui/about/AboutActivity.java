package com.example.santa.anative.ui.about;

import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.santa.anative.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AboutActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_title) TextView mTitleAbout;
    @BindView(R.id.tv_about_content) TextView mTvContent;
    @BindView(R.id.toolbar_about) Toolbar mToolbarAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        initializeToolbar();
        initializeContent();
    }


    private void initializeToolbar() {
        mTitleAbout.setText(R.string.title_about);
        setSupportActionBar(mToolbarAbout);
        mToolbarAbout.inflateMenu(R.menu.edit_equipment);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }


    private void initializeContent() {
        try {
            JSONObject content = new JSONObject(readFile("about.json"));
            mTvContent.setText(content.getString("text")); // TODO CHECK
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private String readFile(String fileName) {
        StringBuilder sbr = new StringBuilder();
        try {
            AssetManager assetManager = getAssets();
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(assetManager.open(fileName)));
            String str = "";
            while ((str = br.readLine()) != null) {
                sbr.append(str);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sbr.toString();
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
