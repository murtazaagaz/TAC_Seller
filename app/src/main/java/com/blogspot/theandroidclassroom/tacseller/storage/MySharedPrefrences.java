package com.blogspot.theandroidclassroom.tacseller.storage;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Murtaza on 1/10/2017.
 */

public class MySharedPrefrences {
    public static SharedPreferences mSharedPrefrences;
    public static MySharedPrefrences mInstance;
    public static Context mContext;

    private String SHARED_PREF_NAME = "tac_seller";
    private String DEALER_NAME = "dealer_name";
    private String DEALER_STORE = "store_name";
    private String DEALER_UID = "dealer_uid";
    private String DEALER_EMAIL = "dealer_email";
    private String DEFAULT = "default";

    public MySharedPrefrences(){
         mSharedPrefrences = mContext.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
    }
    public static MySharedPrefrences getInstance(Context context){
        mContext = context;
        if (mInstance == null){
            mInstance = new MySharedPrefrences();
        }
        return mInstance;
    }
    public void setDealerName (String name) {
        mSharedPrefrences.edit().putString(DEALER_NAME,name).apply();
    }
    public String getDealerName(){
        return mSharedPrefrences.getString(DEALER_NAME,DEFAULT);
    }
    public void setDealerEmail (String email) {
        mSharedPrefrences.edit().putString(DEALER_EMAIL,email).apply();
    }
    public String getDealerEmail(){
        return mSharedPrefrences.getString(DEALER_EMAIL,DEFAULT);
    }
    public void setDealerStoreName (String storeName) {
        mSharedPrefrences.edit().putString(DEALER_STORE,storeName).apply();
    }
    public String getDealerStoreName(){
        return mSharedPrefrences.getString(DEALER_STORE,DEFAULT);
    }
    public void setDealerUid (String uid) {
        mSharedPrefrences.edit().putString(DEALER_UID,uid).apply();
    }
    public String getDealerUid(){
        return mSharedPrefrences.getString(DEALER_UID,DEFAULT);
    }


}
