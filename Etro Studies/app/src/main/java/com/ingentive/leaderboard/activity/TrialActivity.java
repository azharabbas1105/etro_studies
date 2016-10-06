package com.ingentive.leaderboard.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.ingentive.leaderboard.R;
import com.ingentive.leaderboard.adapter.HickoryAdapter;
import com.ingentive.leaderboard.adapter.TrialAdapter;
import com.ingentive.leaderboard.model.TrialModel;
import com.ingentive.leaderboard.utilities.NetworkAvailable;
import com.ingentive.leaderboard.utilities.RememberMeTable;
import com.ingentive.leaderboard.utilities.ServiceHandler;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class TrialActivity extends Activity {

    private ImageView ivBack;
    private ProgressDialog mProgressDialog;
    private RecyclerView myRecyclerView;
    private TextView tvTrial;
    private RememberMeTable rememberMeTable;
    private TrialModel trialModel;
    private List<TrialModel> trialModelList;
    private TrialAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trial);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        initialize();
    }

    private void initialize() {
        try {

            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Please wait...");
            mProgressDialog.setCancelable(false);

            rememberMeTable = new RememberMeTable();
            rememberMeTable = new Select().from(RememberMeTable.class).executeSingle();
            if (NetworkAvailable.isNetworkAvailable(TrialActivity.this)) {
                new UserTestList(rememberMeTable.getSiteNo()).execute();
            } else {
                Toast.makeText(TrialActivity.this, "Please make sure, your network connection is ON", Toast.LENGTH_LONG).show();
            }

            ivBack = (ImageView) findViewById(R.id.iv_back);
            tvTrial = (TextView) findViewById(R.id.tv_trial);
            myRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            myRecyclerView.setLayoutManager(mLayoutManager);
            myRecyclerView.setItemAnimator(new DefaultItemAnimator());

            ivBack.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN: {
                            ivBack.getDrawable().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                            ivBack.invalidate();
                            break;
                        }
                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_CANCEL: {
                            //clear the overlay
                            ivBack.getDrawable().clearColorFilter();
                            ivBack.invalidate();
                            finish();
                            break;
                        }
                    }
                    return true;
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showpDialog() {
        if (!mProgressDialog.isShowing())
            mProgressDialog.show();
    }

    private void hidepDialog() {
        if (mProgressDialog.isShowing())
            mProgressDialog.dismiss();
    }


    private class UserTestList extends AsyncTask<Void, Void, Void> {
        String siteNo = "";
        String content="";
        public UserTestList(String siteNo) {
            this.siteNo = siteNo;
            trialModelList = new ArrayList<>();

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showpDialog();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance

            List<NameValuePair> params = new LinkedList<NameValuePair>();
            params.add(new BasicNameValuePair("site_number", siteNo + ""));

            ServiceHandler sh = new ServiceHandler();
            String url = ServiceHandler.serverURL + ServiceHandler.serviceUserTestList;
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.POST, params);
            android.util.Log.d("Response: ", "> " + jsonStr);
            if (jsonStr != null) {
                try {

//                    trialModel = new TrialModel();
//                    trialModel.setId(1);
//                    trialModel.setTestNme("Bergamot");
//                    trialModel.setRank(2);
//                    trialModel.setTimeStamp(1473098757);
//                    trialModelList.add(trialModel);
//
//                    trialModel = new TrialModel();
//                    trialModel.setId(5);
//                    trialModel.setTestNme("Hickory");
//                    trialModel.setRank(1);
//                    trialModel.setTimeStamp(1473099013);
//                    trialModelList.add(trialModel);
//
//                    trialModel = new TrialModel();
//                    trialModel.setId(3);
//                    trialModel.setTestNme("Hibiscus-I");
//                    trialModel.setRank(1);
//                    trialModel.setTimeStamp(1473098994);
//                    trialModelList.add(trialModel);




                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONObject successObj = jsonObj.getJSONObject("Success");
                    String code = successObj.getString("code");
                     content = successObj.getString("content");
                    String error = jsonObj.getString("Error");
                    if (code.equals("200")) {
                        JSONArray data = jsonObj.getJSONArray("DATA");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject c = data.getJSONObject(i);
                            int id = Integer.parseInt(c.getString("id"));
                            String test_name = c.getString("test_name");
                            int rank = Integer.parseInt(c.getString("rank"));
                            long timestamp = Long.parseLong(c.getString("timestamp"));

                            trialModel = new TrialModel();
                            trialModel.setId(id);
                            trialModel.setTestNme(test_name);
                            trialModel.setRank(rank);
                            trialModel.setTimeStamp(timestamp);
                            trialModelList.add(trialModel);
                        }
                    }else {
                        hidepDialog();
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(TrialActivity.this, "error: " + content, Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    hidepDialog();
                }
            } else {
                hidepDialog();
                android.util.Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            hidepDialog();

            if(trialModelList.size()>0){
                 mAdapter = new TrialAdapter(TrialActivity.this, R.layout.custom_row_trial, trialModelList);
                myRecyclerView.setAdapter(mAdapter);
            }
        }
    }
}
