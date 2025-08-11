package com.example.eleconomico;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private static final String PREF_NAME = "eleconomico_session";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_USER_ID = "id_usuario";
    private static final String KEY_USER_NAME = "nombre_usuario";  // <--- agregamos

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

    public void saveUserId(String userId){
        editor.putString(KEY_USER_ID, userId);
        editor.apply();
    }

    public String getUserId(){
        return prefs.getString(KEY_USER_ID, null);
    }

    // Nuevo método para guardar nombre de usuario
    public void saveUserName(String userName) {
        editor.putString(KEY_USER_NAME, userName);
        editor.apply();
    }

    // Nuevo método para obtener nombre de usuario
    public String getUserName() {
        return prefs.getString(KEY_USER_NAME, null);
    }

    public void clearSession(){
        editor.clear();
        editor.apply();
    }
}
