package com.darwindeveloper.MyPetsApp.servicios;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.darwindeveloper.MyPetsApp.api.Constants;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by DARWIN MOROCHO on 10/8/2017.
 */

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {

    SharedPreferences preferences;

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.i("FirebaseTOKEN", token);
        editor.putString(Constants.INSTANCE.getFIREBASE_TOKEN(), token);
        editor.apply();


    }
}
