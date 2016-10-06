package com.ingentive.leaderboard.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ingentive.leaderboard.R;
import com.ingentive.leaderboard.adapter.HickoryAdapter;
import com.ingentive.leaderboard.adapter.TrialAdapter;
import com.ingentive.leaderboard.model.HickoryModel;
import com.ingentive.leaderboard.model.TrialModel;
import com.ingentive.leaderboard.utilities.CountryFlag;
import com.ingentive.leaderboard.utilities.NetworkAvailable;
import com.ingentive.leaderboard.utilities.ServiceHandler;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class HickoryActivity extends Activity {

    private RecyclerView myRecyclerView;
    private List<HickoryModel> hickoryList;
    private List<HickoryModel> hickoryCountryList;
    private List<HickoryModel> hickoryCountryListRank;
    private List<HickoryModel> hickoryTopFiveList;
    private List<HickoryModel> hickoryTopFiveCountryList;
    private HickoryModel hickoryModel;
    private HickoryAdapter mAdapter;
    private Button btnGlobal, btnCountry;
    private ProgressDialog mProgressDialog;
    private ImageView ivBack, ivShowTopFive;
    private int id = 0;
    private boolean topFive = false;
    private RelativeLayout show_top_five_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hickory);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        try {
            Bundle bundle = getIntent().getExtras();
            if (bundle.getInt("id_") != 0) {
                id = bundle.getInt("id_");
            }
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Please wait...");
            mProgressDialog.setCancelable(false);
            hickoryList = new ArrayList<>();
            hickoryCountryList = new ArrayList<>();
            hickoryTopFiveList = new ArrayList<>();
            hickoryTopFiveCountryList = new ArrayList<>();

            btnGlobal = (Button) findViewById(R.id.btn_global);
            btnCountry = (Button) findViewById(R.id.btn_country);
            ivBack = (ImageView) findViewById(R.id.iv_back);
            ivShowTopFive = (ImageView) findViewById(R.id.iv_show_top_five);
            show_top_five_layout = (RelativeLayout) findViewById(R.id.show_top_five_layout);

            btnGlobal.setSelected(true);
            btnCountry.setSelected(false);

            if (topFive == false) {
                ivShowTopFive.setBackgroundResource(R.drawable.uncheck_icon);
            } else {
                ivShowTopFive.setBackgroundResource(R.drawable.checked_icon);
            }
            show_top_five_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (topFive == false) {
                        topFive = true;
                        if (btnGlobal.isSelected() == true) {
                            showData(hickoryTopFiveList);
                        } else {
                            showData(hickoryTopFiveCountryList);
                        }
                        ivShowTopFive.setBackgroundResource(R.drawable.checked_icon);
                    } else {
                        topFive = false;
                        ivShowTopFive.setBackgroundResource(R.drawable.uncheck_icon);
                        if (btnGlobal.isSelected() == true) {
                            showData(hickoryList);
                        } else {
                            showData(hickoryCountryListRank);
                        }
                    }
                }
            });
            if (NetworkAvailable.isNetworkAvailable(HickoryActivity.this)) {
                new TestInfo(id).execute();
            } else {
                Toast.makeText(HickoryActivity.this, "Please make sure, your network connection is ON", Toast.LENGTH_LONG).show();
            }

            btnGlobal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnGlobal.setSelected(true);
                    btnCountry.setSelected(false);
                    if (topFive == false) {
                        showData(hickoryList);
                    } else {
                        showData(hickoryTopFiveList);
                    }

                }
            });
            btnCountry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnGlobal.setSelected(false);
                    btnCountry.setSelected(true);
                    if (topFive == false) {
                        showData(hickoryCountryListRank);
                    } else {
                        showData(hickoryTopFiveCountryList);
                    }

                }
            });

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
            myRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            myRecyclerView.setLayoutManager(mLayoutManager);
            myRecyclerView.setItemAnimator(new DefaultItemAnimator());
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

    private class TestInfo extends AsyncTask<Void, Void, Void> {
        int testId = 0;
        String content = "";
        int topfiveCountry = 0;
        int topfiveGlobal = 0;

        public TestInfo(int testId) {
            this.testId = testId;
            hickoryList = new ArrayList<>();
            hickoryCountryList = new ArrayList<>();
            hickoryTopFiveList = new ArrayList<>();
            hickoryTopFiveCountryList = new ArrayList<>();
            hickoryCountryListRank= new ArrayList<>();

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
            params.add(new BasicNameValuePair("test_id", testId + ""));

            ServiceHandler sh = new ServiceHandler();
            String url = ServiceHandler.serverURL + ServiceHandler.serviceTestInfo;
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.POST, params);
            android.util.Log.d("Response: ", "> " + jsonStr);
            if (jsonStr != null) {
                try {
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
                            String investigator_first_name = c.getString("investigator_first_name");
                            String investigator_last_name = c.getString("investigator_last_name");
                            int patients_randomised = Integer.parseInt(c.getString("patients_randomised"));
                            String country = c.getString("country");
                            int rank = Integer.parseInt(c.getString("rank"));
                            int deltaDiff = Integer.parseInt(c.getString("deltaDiff"));
                            String since_start = c.getString("since_start");
                            String date_active = c.getString("date_active");


                            hickoryModel = new HickoryModel();
                            hickoryModel.setId(id);
                            hickoryModel.setInvestigator_first_name(investigator_first_name);
                            hickoryModel.setInvestigator_last_name(investigator_last_name);
                            hickoryModel.setPatients_randomised(patients_randomised);
                            hickoryModel.setCountry(country);
                            hickoryModel.setRank(rank);
                            hickoryModel.setSince_start(since_start);
                            hickoryModel.setDate_active(date_active);
                            hickoryModel.setDeltaDiff(deltaDiff);
                            hickoryModel.setFlag(CountryFlag.getFlage(country.toLowerCase()));
                            hickoryList.add(hickoryModel);

                            if (topfiveGlobal < 5) {
                                hickoryTopFiveList.add(hickoryModel);
                                topfiveGlobal++;
                            }
                            if (country.equals(LoginActivity.mCountry)) {
                                hickoryCountryList.add(hickoryModel);
                            }


                        }
                        int mRank=1;
                        int count=0;
//                            HickoryModel preRank=new HickoryModel();
//                            preRank=hickoryCountryList.get(hickoryCountryList.size()-1);
                        for (int k=hickoryCountryList.size();k>1;k--) {
                            HickoryModel currRank=new HickoryModel();

                            currRank=hickoryCountryList.get(k-1);
                            if(hickoryCountryList.size()==k){
                                currRank.setRank(mRank);
                                hickoryCountryListRank.add(currRank);
                                if (topfiveCountry < 5) {
                                    hickoryTopFiveCountryList.add(currRank);
                                    topfiveCountry++;
                                }
                            }
                            else if(hickoryCountryList.get(k).getRank()==hickoryCountryList.get(k-1).getRank()){
                                currRank.setRank(mRank);
                                hickoryCountryListRank.add(currRank);
                                if (topfiveCountry < 5) {
                                    hickoryTopFiveCountryList.add(currRank);
                                    topfiveCountry++;
                                }
                            }else if(hickoryCountryList.get(k).getRank()<hickoryCountryList.get(k-1).getRank()){
                                mRank++;
                                currRank.setRank(mRank);
                                hickoryCountryListRank.add(currRank);
                                if (topfiveCountry < 5) {
                                    hickoryTopFiveCountryList.add(currRank);
                                    topfiveCountry++;
                                }
                            }
                        } } else {
                        hidepDialog();
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(HickoryActivity.this, "error: " + content, Toast.LENGTH_LONG).show();
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

            showData(hickoryList);
        }
    }

    private void showData(List<HickoryModel> list) {
        try {
            mAdapter = new HickoryAdapter(HickoryActivity.this, R.layout.custom_row_hickory, list);
            myRecyclerView.setAdapter(mAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
