package com.example.eleconomico;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private static final String PREF_NAME = "eleconomico_session";
    private static final String KEY_EMAIL = "email";

    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    public SessionManager(Context context){
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void saveUserEmail(String email){
        editor.putString(KEY_EMAIL, email);
        editor.apply();
    }

    public String getUserEmail(){
        return prefs.getString(KEY_EMAIL, null);
    }

    public void clearSession(){
        editor.clear();
        editor.apply();
    }
}
