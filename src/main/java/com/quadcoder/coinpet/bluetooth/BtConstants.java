package com.quadcoder.coinpet.bluetooth;

public interface BTConstants {

    // Message types sent from the BluetoothManager Handler
    int MESSAGE_STATE_CHANGE = 1;
    int MESSAGE_READ = 2;
    int MESSAGE_WRITE = 3;
    int MESSAGE_DEVICE_NAME = 4;
    int MESSAGE_TOAST = 5;

    // Constants that indicate the current connection state
    int STATE_NONE = 0;       // we're doing nothing
    int STATE_BT_ENABLED = 1;     // now listening for incoming connections
    int STATE_CONNECTING = 2; // now initiating an outgoing connection
    int STATE_CONNECTED = 3;  // now connected to a remote device
    int STATE_LISTEN = 4;
    int STATE_PAIRING = 5;
    int STATE_DISCOVERING = 6;

    // Key names received from the BluetoothManager Handler
    String DEVICE_NAME = "COINPET";
    String TOAST = "toast";
}
