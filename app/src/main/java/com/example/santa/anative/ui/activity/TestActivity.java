package com.example.santa.anative.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.widget.LinearLayout;

import com.example.santa.anative.R;

import butterknife.ButterKnife;

public class TestActivity extends AppCompatActivity {

    LinearLayout mLinearLayout;
    private PopupMenu popupMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);

    }


}
