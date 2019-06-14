package com.example.sqlbrite.app;

import android.app.Application;
import android.support.multidex.MultiDex;

import com.facebook.stetho.Stetho;
import com.mob.MobSDK;

public class MainApplication extends Application {

    @Override
    public void onCreate(){
        super.onCreate();
        MultiDex.install(this);
        Stetho.initializeWithDefaults(this);
        MobSDK.init(this);
    }
}
