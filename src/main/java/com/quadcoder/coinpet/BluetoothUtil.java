package com.quadcoder.coinpet;

/**
 * Created by Phangji on 3/27/15.
 */
public class BluetoothUtil {
    private static BluetoothUtil instance;
    public BluetoothUtil getIntance(){
        if(instance == null)
            instance = new BluetoothUtil();
        return instance;
    }







}
