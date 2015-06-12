package com.quadcoder.coinpet.page.common;

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

    public String getToken() {  // TODO: 로그인 하는 프로세스로 진입시, 이 토큰 제거해줘야함.
        token = mPrefs.getString(FIELD_TOKEN, "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJma19raWRzIjoxNDUsIm5hbWUiOiLqt6Tqt6QyIiwiaWF0IjoxNDMxNDkyNjQ5fQ.ThWR16YikjVb77lkK1qTp5H3GPVk2i9gFl7C5QDKdlM");
        return "Bearer " + token;
    }

    public void setToken(String token) {
        this.token = token;
        mEditor.putString(FIELD_TOKEN, token);
        mEditor.commit();
    }

    /**
     * goal
     * */

    public Goal mGoal;


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

    public void moneyUp(int upMoney) {
        setNowMoney(getNowMoney() + upMoney);
    }

    /**
     * nowPoint
     * */

    private static final String FIELD_NOW_POINT = "nowPoint";   //프로그레스바의 현재 값
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

    public void pointUp(int point) {
        nowPoint += point;
        setNowPoint(nowPoint);
    }

    /**
     * nowLevel
     * */

    private static final String FIELD_NOW_LEVEL = "nowLevel";
    private int nowLevel;

    public int getNowLevel() {
        nowLevel = mPrefs.getInt(FIELD_NOW_LEVEL, 1);
        return nowLevel;

    }

    public void setNowLevel(int nowLevel) {
        this.nowLevel = nowLevel;
        mEditor.putInt(FIELD_NOW_LEVEL, nowLevel);
        mEditor.commit();
    }

    public void levelUp() {
        nowLevel += 1;
        setNowLevel(nowLevel);
    }

    /**
     * QCoin
     */

    private static final String FIELD_QCOIN = "qCoin";
    private int qCoin = 3;  //TODO: 하루가 지나면 다시 3개가 되어야함.

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
     * pkQuest
     */

    private static final String FIELD_PK_QUEST = "pkQuest";
    private int pkQuest;

    public int getPkQuest() {
        pkQuest = mPrefs.getInt(FIELD_PK_QUEST, 0);
        return pkQuest;
    }

    public void setPkQuest(int pkQuest) {
        this.pkQuest = pkQuest;
        mEditor.putInt(FIELD_PK_QUEST, pkQuest);
        mEditor.commit();
    }

    /**
     * pkPQuest
     */

    private static final String FIELD_PK_P_QUEST = "pkPQuest";
    private int pkPQuest;

    public int getPkPQuest() {
        pkPQuest = mPrefs.getInt(FIELD_PK_P_QUEST, 0);
        return pkPQuest;
    }

    public void setPkPQuest(int pkPQuest) {
        this.pkPQuest = pkPQuest;
        mEditor.putInt(FIELD_PK_P_QUEST, pkPQuest);
        mEditor.commit();
    }

    /**
     * pkQuiz
     */

    private static final String FIELD_PK_QUIZ = "pkQuiz";
    private int pkQuiz;

    public int getPkQuiz() {
        pkQuiz = mPrefs.getInt(FIELD_PK_QUIZ, 0);
        return pkQuiz;
    }

    public void setPkQuiz(int pkQuiz) {
        this.pkQuiz = pkQuiz;
        mEditor.putInt(FIELD_PK_QUIZ, pkQuiz);
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
