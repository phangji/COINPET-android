package com.quadcoder.coinpet;

import android.content.Context;
import android.content.SharedPreferences;

import com.quadcoder.coinpet.network.response.Goal;

/**
 * Created by Phangji on 4/1/15.
 */
public class PropertyManager {
    private static PropertyManager instance;

    public static PropertyManager getInstance(){
        if(instance == null) {
            instance = new PropertyManager();
        }
        return instance;
    }

    private static final String PREF_NAME = "coinpet_prefs";
    SharedPreferences mPrefs;
    SharedPreferences.Editor mEditor;

    private PropertyManager() {
        mPrefs = MyApplication.getContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        mEditor = mPrefs.edit();
    }

    private static final String FIELD_PN = "pn";
    private String pn = null;

    public String getPn() {
//        pn = mPrefs.getString(FIELD_PN, "");
        return pn;
    }

    public void setPn(String pn) {
        this.pn = pn;
//        mEditor.putString(FIELD_PN, this.pn);
//        mEditor.commit();
    }

    private static final String FIELD_MAC = "mac";
    private String mac = null;

    public String getMac() {
        mac = mPrefs.getString(FIELD_MAC, "");
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
        mEditor.putString(FIELD_MAC, this.mac);
        mEditor.commit();
    }

    // TODO: 플로우 검사가 끝난 후, token을 영구적으로 저장해줘야 함.
    private static final String FIELD_TOKEN = "token";
    private String token = null;

    public String getToken() {
//        token = mPrefs.getString(FIELD_TOKEN, "");
        return "Bearer " + token;
    }

    public void setToken(String token) {
//        mEditor.putString(FIELD_TOKEN, this.token);
//        mEditor.commit();
        this.token = token;
    }

    public Goal mGoal = new Goal();
}
