package com.blogspot.theandroidclassroom.tacseller.infrastructure;

import android.app.Application;
import android.content.Context;

/**
 * Created by Murtaza on 1/10/2017.
 */

public class MyApplication extends Application {
    private static MyApplication mInstance;
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }
    public static MyApplication getInstance(){
        return mInstance;
    }
    public static Context getContext (){
        return mInstance.getApplicationContext();
    }

}
