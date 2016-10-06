package com.ingentive.leaderboard.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ingentive.leaderboard.R;
import com.ingentive.leaderboard.activity.HickoryActivity;
import com.ingentive.leaderboard.model.HickoryModel;
import com.ingentive.leaderboard.model.TrialModel;

import java.util.List;

/**
 * Created by PC on 03-10-2016.
 */
public class TrialAdapter extends RecyclerView.Adapter<TrialAdapter.MyViewHolder>{

    private Context mContext;
    int layoutRes;
    View row;
    List<TrialModel> trialModelList;

    public TrialAdapter(Context context, int resource,
                        List<TrialModel> trialModelList) {
        this.mContext = context;
        this.layoutRes = resource;
        this.trialModelList = trialModelList;
    }



    public class MyViewHolder extends RecyclerView.ViewHolder  {
        public TextView tvGlobalRank, tvRank, tvName;
        public LinearLayout trial_layout,rank_layout;

        public MyViewHolder(View view) {
            super(view);
            try {
                tvGlobalRank = (TextView) view.findViewById(R.id.tv_global_rank);
                tvRank = (TextView) view.findViewById(R.id.tv_rank);
                tvName = (TextView) view.findViewById(R.id.tv_name);
                trial_layout = (LinearLayout)view.findViewById(R.id.trial_layout);
                rank_layout = (LinearLayout)view.findViewById(R.id.rank_layout);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_row_trial, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        try {
            final TrialModel trialModel = trialModelList.get(position);
            holder.tvRank.setText(trialModel.getRank()+"");
            holder.tvName.setText(trialModel.getTestNme() + "");
            holder.trial_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent =new Intent(mContext, HickoryActivity.class);
                    intent.putExtra("id_",trialModel.getId());
                    mContext.startActivity(intent);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public int getItemCount() {
        return trialModelList.size();
    }
}
