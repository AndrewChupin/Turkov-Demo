package com.example.santa.anative.ui.activity;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.santa.anative.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
        Button button = (Button) findViewById(R.id.button);
        final EditText editText = (EditText) findViewById(R.id.editText);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hello(editText.getText().toString());
            }
        });
    }

    private void hello(String ha) {

    }

    static {
        System.loadLibrary("secure-wrapper");
    }


    public native String encrypt(String s);
}
