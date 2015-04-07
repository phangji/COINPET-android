package com.quadcoder.coinpet;

import android.content.Context;
import android.content.SharedPreferences;

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
        pn = mPrefs.getString(FIELD_PN, "");
        return pn;
    }

    public void setPn(String pn) {
        this.pn = pn;
        mEditor.putString(FIELD_PN, this.pn);
        mEditor.commit();
    }

    private static final String FIELD_DMAC = "dmac";
    private String dmac = null;

    public String getDmac() {
        dmac = mPrefs.getString(FIELD_DMAC, "");
        return dmac;
    }

    public void setDmac(String dmac) {
        this.dmac = dmac;
        mEditor.putString(FIELD_DMAC, this.dmac);
        mEditor.commit();
    }

    private static final String FIELD_IS_FIRST = "isFirst";
    private boolean isFirst = true;

    public boolean isFirst() {
        isFirst = mPrefs.getBoolean(FIELD_IS_FIRST, true);
        return isFirst;
    }

    public void setFirst(boolean isFirst) {
        this.isFirst = isFirst;
        mEditor.putBoolean(FIELD_IS_FIRST, isFirst);
        mEditor.commit();
    }

    public String tempMac;
    public boolean isSet;
}
