package com.example.santa.anative.widget.adapter.recycler;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.santa.anative.R;
import com.example.santa.anative.model.entity.Setting;
import com.example.santa.anative.ui.common.StateChangedListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import io.realm.RealmList;

/**
 * Created by santa on 11.03.17.
 */

public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.SettingsViewHolder> {

    private RealmList<Setting> mSettings;
    private StateChangedListener mStateChangedListener;

    public SettingsAdapter(RealmList<Setting> settings) {
        mSettings = settings;
    }

    public void setOnStateChangeListener(StateChangedListener stateChangedListener) {
        mStateChangedListener = stateChangedListener;
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
        }

        @OnCheckedChanged(R.id.switch_setting_enable)
        void onSettingStateChanged() {
            if (mStateChangedListener != null) {
                Setting setting = mSettings.get(getAdapterPosition());
                setting.setActive(mSwitchEnable.isChecked());
                mStateChangedListener.onStateChanged(setting);
            }
        }
    }
}
