package com.ingentive.leaderboard.utilities;

import android.app.Application;
import android.content.res.Configuration;

import com.activeandroid.ActiveAndroid;

/**
 * Created by PC on 07-06-2016.
 */
//@ReportsCrashes(formKey = "",
//        formUri = "http://yourbrand.pk/yourbrand/junaid_khan/nahadcrashes/crashscript.php",
//        mode = ReportingInteractionMode.TOAST,
//        resToastText = R.string.crash_toast_text)
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this);
//        ACRA.init(this);
    }
}