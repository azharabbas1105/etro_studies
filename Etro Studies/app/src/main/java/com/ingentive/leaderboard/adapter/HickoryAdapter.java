package com.ingentive.leaderboard.adapter;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ingentive.leaderboard.R;
import com.ingentive.leaderboard.model.HickoryModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC on 03-10-2016.
 */
public class HickoryAdapter extends RecyclerView.Adapter<HickoryAdapter.MyViewHolder> {

    private Context mContext;
    int layoutRes;
    View row;
    List<HickoryModel> hickoryList = new ArrayList<>();
    private int selectedPos = -1;

    public HickoryAdapter(Context context, int resource,
                          List<HickoryModel> hickoryList) {

        this.mContext = context;
        this.layoutRes = resource;
        this.hickoryList = hickoryList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvRank, tvName, tvCountry, tvEnrolledThisMont, tvSinceStare, tvSite;
        public ImageView ivFlag;
        public RelativeLayout hickory_row_layout,rank_layout;
        private SparseBooleanArray selectedItems = new SparseBooleanArray();

        public MyViewHolder(View view) {
            super(view);
            try {
                tvRank = (TextView) view.findViewById(R.id.tv_rank);
                tvName = (TextView) view.findViewById(R.id.tv_name);
                tvCountry = (TextView) view.findViewById(R.id.tv_country);
                tvEnrolledThisMont = (TextView) view.findViewById(R.id.tv_total_entrolled_this_month);
                tvSinceStare = (TextView) view.findViewById(R.id.tv_total_entrolled_since_start);
                tvSite = (TextView) view.findViewById(R.id.tv_site_activation);
                hickory_row_layout = (RelativeLayout) view.findViewById(R.id.hickory_row_layout);
                rank_layout = (RelativeLayout) view.findViewById(R.id.rank_layout);
                ivFlag = (ImageView) view.findViewById(R.id.iv_flag);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_row_hickory, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final HickoryModel hickoryModel = hickoryList.get(position);
        if (hickoryModel.getRank() == 0) {
            holder.tvRank.setText("-");
        } else {
            holder.tvRank.setText(hickoryModel.getRank() + "");
        }
        try {
            holder.tvName.setText(hickoryModel.getInvestigator_first_name() + " " + hickoryModel.getInvestigator_last_name());
            holder.tvCountry.setText(hickoryModel.getCountry() + "");
            holder.tvEnrolledThisMont.setText(hickoryModel.getPatients_randomised() + "");
            holder.tvSinceStare.setText(hickoryModel.getSince_start() + "");
            holder.tvSite.setText("Site Activation: " + hickoryModel.getDate_active() + "");
            holder.ivFlag.setBackgroundResource(hickoryModel.getFlag());

            if (selectedPos == position) {
                holder.hickory_row_layout.setSelected(true);
                holder.tvName.setTextColor(mContext.getResources().getColor(R.color.colorWhite));
                holder.tvRank.setTextColor(mContext.getResources().getColor(R.color.colorWhite));
                holder.tvCountry.setTextColor(mContext.getResources().getColor(R.color.colorWhite));
                holder.tvSinceStare.setTextColor(mContext.getResources().getColor(R.color.colorWhite));
                holder.tvEnrolledThisMont.setTextColor(mContext.getResources().getColor(R.color.colorWhite));
                holder.tvSite.setTextColor(mContext.getResources().getColor(R.color.colorWhite));
                holder.rank_layout.setBackgroundResource(R.drawable.blue_circle_icon);
            } else {
                holder.hickory_row_layout.setSelected(false);
                holder.tvName.setTextColor(mContext.getResources().getColor(R.color.colorNameSiteRank));
                holder.tvRank.setTextColor(mContext.getResources().getColor(R.color.colorNameSiteRank));
                holder.tvCountry.setTextColor(mContext.getResources().getColor(R.color.colorCountry));
                holder.tvSinceStare.setTextColor(mContext.getResources().getColor(R.color.colorNameSiteRank));
                holder.tvSite.setTextColor(mContext.getResources().getColor(R.color.colorNameSiteRank));
                holder.tvEnrolledThisMont.setTextColor(mContext.getResources().getColor(R.color.colorNameSiteRank));
                holder.rank_layout.setBackgroundResource(R.drawable.green_circle_icon);
            }

            holder.hickory_row_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Updating old as well as new positions
                    notifyItemChanged(selectedPos);
                    if(selectedPos==position){
                        selectedPos=-1;
                    }else {
                        selectedPos = position;
                    }

                    notifyItemChanged(selectedPos);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return hickoryList.size();
    }
}
