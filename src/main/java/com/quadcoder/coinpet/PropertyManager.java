package com.quadcoder.coinpet;

import android.content.Context;
import android.content.SharedPreferences;

import com.quadcoder.coinpet.network.response.Goal;

/**
 * Created by Phangji on 4/1/15.
 */
public class PropertyManager {

    private static class PropertyManagerHolder {
        private static final PropertyManager instance = new PropertyManager();
    }
    public static PropertyManager getInstance(){
        return PropertyManagerHolder.instance;
    }

    private static final String PREF_NAME = "coinpet_prefs";
    SharedPreferences mPrefs;
    SharedPreferences.Editor mEditor;

    private PropertyManager() {
        mPrefs = MyApplication.getContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        mEditor = mPrefs.edit();
    }

    /**
     * pn
     * */

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

    /**
     * mac
     * */

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

    /**
     * token
     * */

    // CREATE: 플로우 검사가 끝난 후, token을 영구적으로 저장해줘야 함.
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

    /**
     * goal
     * */

    public Goal mGoal = new Goal();


    /**
     * sound
     * */

    public static final String FIELD_SOUND = "sound";
    private boolean sound;

    public boolean isSound() {
        sound = mPrefs.getBoolean(FIELD_SOUND, true);
        return sound;
    }

    public void setSound(boolean sound) {
        this.sound = sound;
        mEditor.putBoolean(FIELD_SOUND, sound);
        mEditor.commit();
    }

    /**
     * name
     * */

    private static final String FIELD_NAME = "name";
    private String name = null;

    public String getName() {
        name = mPrefs.getString(FIELD_NAME, "");
        return name;
    }

    public void setName(String name) {
        this.name = name;
        mEditor.putString(FIELD_NAME, name);
        mEditor.commit();
    }

    /**
     * nowMoney
     * */

    private static final String FIELD_NOW_MONEY = "nowMoney";
    private int nowMoney;

    public int getNowMoney() {
        nowMoney = mPrefs.getInt(FIELD_NOW_MONEY, 0);
        return nowMoney;
    }

    public void setNowMoney(int nowMoney) {
        this.nowMoney = nowMoney;
        mEditor.putInt(FIELD_NOW_MONEY, nowMoney);
        mEditor.commit();
    }

    /**
     * nowPoint
     * */

    private static final String FIELD_NOW_POINT = "nowPoint";
    private int nowPoint;

    public int getNowPoint() {
        nowPoint = mPrefs.getInt(FIELD_NOW_POINT, 0);
        return nowPoint;
    }

    public void setNowPoint(int nowPoint) {
        this.nowPoint = nowPoint;
        mEditor.putInt(FIELD_NOW_POINT, nowPoint);
        mEditor.commit();
    }

    /**
     * nowLevel
     * */

    private static final String FIELD_NOW_LEVEL = "nowLevel";
    private int nowLevel;

    public int getNowLevel() {
        nowLevel = mPrefs.getInt(FIELD_NOW_LEVEL, 0);
        return nowLevel;

    }

    public void setNowLevel(int nowLevel) {
        this.nowLevel = nowLevel;
        mEditor.putInt(FIELD_NOW_LEVEL, nowLevel);
        mEditor.commit();
    }

    /**
     * QCoin
     */

    private static final String FIELD_QCOIN = "qCoin";
    private int qCoin = 3;

    public int getqCoin() {
        qCoin = mPrefs.getInt(FIELD_QCOIN, 3);
        return qCoin;
    }

    public void setqCoin(int qCoin) {
        this.qCoin = qCoin;
        mEditor.putInt(FIELD_QCOIN, qCoin);
        mEditor.commit();
    }

    /**
     *  영구저장 X
     */

    private static boolean btRequested = true;
    public boolean isBtReqested() {
        return btRequested;
    }

    public void setBtRequested(boolean btRequested) {
        this.btRequested = btRequested;
    }
}
