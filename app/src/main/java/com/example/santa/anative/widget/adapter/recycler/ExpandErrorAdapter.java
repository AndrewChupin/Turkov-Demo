package com.example.santa.anative.widget.adapter.recycler;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.santa.anative.R;
import com.example.santa.anative.model.entity.EquipmentError;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by santa on 12.03.17.
 */

public class ExpandErrorAdapter extends RecyclerView.Adapter<ExpandErrorAdapter.ExpandChildViewHolder> {

    private ArrayList<EquipmentError> mErrorList;
    private Context mContext;

    public ExpandErrorAdapter(Context context, ArrayList<EquipmentError> errorList) {
        mContext = context;
        mErrorList = errorList;
    }

    @Override
    public ExpandChildViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expand_child, parent, false);
        return new ExpandChildViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ExpandChildViewHolder holder, int position) {
        if (position % 2 == 0) holder.itemView.setBackground(ContextCompat.getDrawable(mContext, R.color.white));
        else holder.itemView.setBackground(ContextCompat.getDrawable(mContext, R.color.focus));

        String date = String.valueOf(mErrorList.get(position).getDate());

        holder.mTvErrorName.setText(mErrorList.get(position).getName());
        holder.mTvErrorTime.setText(date);
        holder.mTvErrorDate.setText(date);
    }

    @Override
    public int getItemCount() {
        return mErrorList.size();
    }



    class ExpandChildViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_error_item_name) TextView mTvErrorName;
        @BindView(R.id.tv_error_item_time) TextView mTvErrorTime;
        @BindView(R.id.tv_error_item_date) TextView mTvErrorDate;

        ExpandChildViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
