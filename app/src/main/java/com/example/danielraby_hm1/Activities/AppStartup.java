package com.example.danielraby_hm1.Activities;

import android.app.Application;

import com.example.danielraby_hm1.Utilities.ScoreSharedPreferences;

public class AppStartup extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ScoreSharedPreferences.init(this);
    }
}
