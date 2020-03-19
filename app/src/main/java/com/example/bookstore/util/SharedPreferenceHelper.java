package com.example.bookstore.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceHelper  {

    private String sharedLogin = "Login";

    public void  initialize(Context context,String name,String password) {
        SharedPreferences sp = context.getSharedPreferences(sharedLogin, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("name",name);
        editor.putString("password",password);
        editor.apply();
    }

    public void removeSharedPreference(Context context){
        SharedPreferences sp = context.getSharedPreferences(sharedLogin,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();
    }

    public String getSharedName(Context context){
        SharedPreferences sp = context.getSharedPreferences(sharedLogin,Context.MODE_PRIVATE);
        return sp.getString("name","");
    }

    public void  setSharedUserId(Context context,int id) {
        SharedPreferences sp = context.getSharedPreferences(sharedLogin, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("id",id);
        editor.apply();
    }

}
