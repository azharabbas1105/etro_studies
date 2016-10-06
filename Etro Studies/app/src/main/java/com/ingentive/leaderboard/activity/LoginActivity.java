package com.ingentive.leaderboard.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.ingentive.leaderboard.R;
import com.ingentive.leaderboard.utilities.NetworkAvailable;
import com.ingentive.leaderboard.utilities.RememberMeTable;
import com.ingentive.leaderboard.utilities.ServiceHandler;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class LoginActivity extends Activity {

    private ProgressDialog mProgressDialog;
    private EditText etSiteNo, etPassword;
    private ImageView ivRememberMe;
    private LinearLayout seeStandLayout;
    private RememberMeTable rememberMeTable;
    public static String mCountry = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        initialize();
    }

    private void initialize() {
        try {
            rememberMeTable = new RememberMeTable();
            rememberMeTable = new Select().from(RememberMeTable.class).executeSingle();
            if (rememberMeTable == null) {
                RememberMeTable rememberMeTable = new RememberMeTable();
                rememberMeTable.isChecked = false;
                rememberMeTable.password = null;
                rememberMeTable.siteNo = null;
                rememberMeTable.save();
            }
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Please wait...");
            mProgressDialog.setCancelable(false);

            etSiteNo = (EditText) findViewById(R.id.et_site_number);
            etPassword = (EditText) findViewById(R.id.et_password);
            ivRememberMe = (ImageView) findViewById(R.id.iv_checkbox);
            seeStandLayout = (LinearLayout) findViewById(R.id.see_where_i_stand_layout);

            rememberMeTable = new RememberMeTable();
            rememberMeTable = new Select().from(RememberMeTable.class).executeSingle();

            if (rememberMeTable.isChecked() == true) {
                ivRememberMe.setBackgroundResource(R.drawable.remember_checked_icon);
                if (rememberMeTable.getSiteNo() != "" && !rememberMeTable.getSiteNo().isEmpty()) {
//                etSiteNo.setText("Site Number: "+rememberMeTable.getSiteNo());
//                etPassword.setText("Password: "+rememberMeTable.getPassword());
                    etSiteNo.setHint("Site Number: " + rememberMeTable.getSiteNo());
                    rememberMeTable.getPassword();
                    String str = "";
                    for (int i = 0; i < rememberMeTable.getPassword().length(); i++) {
                        str = str + "*";
                    }
                    etPassword.setHint("Password: " + str);
                }
            } else {
                ivRememberMe.setBackgroundResource(R.drawable.remember_unchecked_icon);
            }
            seeStandLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rememberMeTable = new RememberMeTable();
                    rememberMeTable = new Select().from(RememberMeTable.class).executeSingle();

                    if (etSiteNo.getText().toString().trim().length() != 0 || etPassword.getText().toString().length() != 0) {
                        if (NetworkAvailable.isNetworkAvailable(LoginActivity.this)) {
                            if (rememberMeTable.getSiteNo() != "" && !rememberMeTable.getSiteNo().isEmpty()) {
                                rememberMeTable.password = "";
                                rememberMeTable.siteNo = "";
                                rememberMeTable.save();
                            }
                            new UserLogin(etSiteNo.getText().toString().trim(), etPassword.getText().toString()).execute(); // etrostudies
                        } else {
                            Toast.makeText(LoginActivity.this, "Please make sure, your network connection is ON", Toast.LENGTH_LONG).show();
                        }
                    } else if (rememberMeTable.isChecked() == true && (rememberMeTable.getSiteNo() != "" && !rememberMeTable.getSiteNo().equals(""))) {
                        ivRememberMe.setBackgroundResource(R.drawable.remember_checked_icon);
                        if (NetworkAvailable.isNetworkAvailable(LoginActivity.this)) {
                            //showpDialog();
                            new UserLogin(rememberMeTable.getSiteNo() + "", rememberMeTable.getPassword()).execute();
                            //new UserTestList(etSiteNo.getText().toString().trim());
                        } else {
                            Toast.makeText(LoginActivity.this, "IPlease make sure, your network connection is ON", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Input field empty", Toast.LENGTH_LONG).show();
                    }
                }

            });

            ivRememberMe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rememberMeTable = new RememberMeTable();
                    rememberMeTable = new Select().from(RememberMeTable.class).executeSingle();
                    if (rememberMeTable.isChecked() == true) {
//                    ivRememberMe.setBackgroundResource(R.drawable.checkbox_checked);
                        ivRememberMe.setBackgroundResource(R.drawable.remember_unchecked_icon);
                        rememberMeTable.isChecked = false;
                        rememberMeTable.password = "";
                        rememberMeTable.siteNo = "";
                        rememberMeTable.save();
                        etSiteNo.setHint("Site Number: ");
                        etPassword.setHint("Password: " );
                    } else {
                        rememberMeTable.isChecked = true;
                        rememberMeTable.save();
                        ivRememberMe.setBackgroundResource(R.drawable.remember_checked_icon);
                    }
                }
            });
        }catch (Exception e){
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

    private class UserLogin extends AsyncTask<Void, Void, Void> {
        String siteNo = "";
        String pass = "";
        String content="";

        public UserLogin(String siteNo, String pass) {
            this.siteNo = siteNo;
            this.pass = pass;

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
            params.add(new BasicNameValuePair("password", pass + ""));
            ServiceHandler sh = new ServiceHandler();
            //String jsonStr = sh.makeServiceCall(ServiceHandler.serverURL + ServiceHandler.serviceUserTestList, ServiceHandler.POST, params);
            String jsonStr = sh.makeServiceCall(ServiceHandler.serverURL + ServiceHandler.serviceIsAuthentic, ServiceHandler.POST, params);
            android.util.Log.d("Response: ", "> " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONObject successObj = jsonObj.getJSONObject("Success");
                    String code = successObj.getString("code");
                     content = successObj.getString("content");
                    String error = jsonObj.getString("Error");
                    if (code.equals("200")) {
                        JSONObject dataObj = jsonObj.getJSONObject("DATA");
                        mCountry = dataObj.getString("country");
                        String status = dataObj.getString("status");
                        String investigator_first_name = dataObj.getString("investigator_first_name");
                        String investigator_last_name = dataObj.getString("investigator_last_name");

                        rememberMeTable = new RememberMeTable();
                        rememberMeTable = new Select().from(RememberMeTable.class).executeSingle();
                        if (rememberMeTable.isChecked() == true && rememberMeTable.getSiteNo().isEmpty() || rememberMeTable.getSiteNo().equals("")) {
                            rememberMeTable.siteNo = siteNo;
                            rememberMeTable.password = pass;
                            rememberMeTable.save();


                            runOnUiThread(new Runnable() {
                                public void run() {
                                    etSiteNo.setText("");
                                    etPassword.setText("");
                                    etSiteNo.setHint("Site Number: " + siteNo);

                                    String str = "";
                                    for (int i = 0; i < rememberMeTable.getPassword().length(); i++) {
                                        str = str + "*";
                                    }
                                    etPassword.setHint("Password: " + str);
                                }
                            });
                        }
                        Intent intent = new Intent(LoginActivity.this, TrialActivity.class);
                        startActivity(intent);
                    } else {
                        hidepDialog();
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(LoginActivity.this, "error: " + content, Toast.LENGTH_LONG).show();
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
        }
    }

}
