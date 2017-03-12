package com.example.santa.anative.ui.adapter.recycler;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatCheckedTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.santa.anative.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by santa on 08.03.17.
 */

public class MySpinnerAdapter extends ArrayAdapter<String> {


    ArrayList<String> mArrayList;
    private Context mContext;


    public MySpinnerAdapter(Context context, List<String> objects) {
        super(context, R.layout.item_spinner_dropdown, objects);
        mArrayList = (ArrayList<String>) objects;
        mContext = context;
    }



    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_spinner_dropdown, parent, false);
        TextView textView = (TextView) view.findViewById(R.id.tv_spinner_item);
        textView.setText(mArrayList.get(position));
        return view;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_spinner_dropdown, parent, false);
        view.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_spinner_selected));
        TextView textView = (TextView) view.findViewById(R.id.tv_spinner_item);
        textView.setText(mArrayList.get(position));
        return view;
    }


}
