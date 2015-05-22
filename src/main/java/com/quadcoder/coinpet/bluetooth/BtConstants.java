package com.quadcoder.coinpet.bluetooth;

public interface BtConstants {

    // Message types sent from the BluetoothManager Handler
    int MESSAGE_STATE_CHANGE = 1;
    int MESSAGE_READ = 2;
    int MESSAGE_WRITE = 3;
    int MESSAGE_DEVICE_NAME = 4;
    int MESSAGE_TOAST = 5;

    // Key names received from the BluetoothManager Handler
    String DEVICE_NAME = "COINPET";
    String TOAST = "toast";

}
