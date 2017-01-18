package com.blogspot.theandroidclassroom.tacseller.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.blogspot.theandroidclassroom.tacseller.infrastructure.MyApplication;

/**
 * Created by Murtaza on 1/10/2017.
 */

public class Util {
    public static boolean isNetworkAvailable(){
        ConnectivityManager manager = (ConnectivityManager) MyApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable()
                && networkInfo.isConnected();
    }
}
