package com.example.santa.anative.widget.adapter.recycler;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.santa.anative.R;
import com.example.santa.anative.model.entity.Equipment;
import com.example.santa.anative.ui.activity.EquipmentActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by santa on 11.03.17.
 */

public class EquipmentAdapter extends RecyclerView.Adapter<EquipmentAdapter.EquipmentViewHolder> {

    private ArrayList<Equipment> mEquipmentList;
    private Context mContext;

    public EquipmentAdapter(Context context, ArrayList<Equipment> equipmentList) {
        mContext = context;
        mEquipmentList = equipmentList;
    }

    @Override
    public EquipmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_equipment, parent, false);
        return new EquipmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EquipmentViewHolder holder, int position) {
        holder.mTvName.setText(mEquipmentList.get(position).getName());
        holder.mTvHumidity.setText(mEquipmentList.get(position).getHumidity());
        holder.mTvTemperature.setText(mEquipmentList.get(position).getTemperature());
        if (mEquipmentList.get(position).isEnable()) holder.mSwitcher.setChecked(true);
    }

    @Override
    public int getItemCount() {
        return mEquipmentList.size();
    }

    class EquipmentViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_equipment_item_humidity) TextView mTvHumidity;
        @BindView(R.id.tv_equipment_item_name) TextView mTvName;
        @BindView(R.id.tv_equipment_item_temperature) TextView mTvTemperature;
        @BindView(R.id.rb_equipment_item_switcher) RadioButton mSwitcher;

        EquipmentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.cv_equipment_item)
        void onDetailEquipment() {
            mContext.startActivity(new Intent(mContext , EquipmentActivity.class));
        }
    }
}
