package com.example.santa.anative.widget.adapter.recycler;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.santa.anative.R;
import com.example.santa.anative.model.entity.Setting;
import com.example.santa.anative.ui.equipment.setting.SettingEquipmentActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmList;

/**
 * Created by santa on 11.03.17.
 */

public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.SettingsViewHolder> {

    private RealmList<Setting> mSettings;
    private Context mContext;

    public SettingsAdapter(Context context, RealmList<Setting> settings) {
        mContext = context;
        mSettings = settings;
    }

    @Override
    public SettingsAdapter.SettingsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_setting, parent, false);
        return new SettingsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SettingsAdapter.SettingsViewHolder holder, int position) {
        Setting setting = mSettings.get(position);
        holder.mCheckActive.setChecked(setting.isActive());
        holder.mSwitchEnable.setChecked(setting.isEnable());
        holder.mTvIndex.setText(setting.getTemperature() + "/" + setting.getSpeed());
        holder.mTvTime.setText(setting.getHour() + ":" + setting.getMinutes());
    }

    @Override
    public int getItemCount() {
        return mSettings.size();
    }

    class SettingsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_setting_index) TextView mTvIndex;
        @BindView(R.id.tv_setting_time) TextView mTvTime;
        @BindView(R.id.checkbox_setting_active) RadioButton mCheckActive;
        @BindView(R.id.switch_setting_enable) SwitchCompat mSwitchEnable;

        SettingsViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext , SettingEquipmentActivity.class);
                    intent.putExtra(SettingEquipmentActivity.EXTRA_SETTING_ID, mSettings.get(getAdapterPosition()).getId());
                    mContext.startActivity(intent);
                }
            });
        }
    }
}
