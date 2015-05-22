package com.quadcoder.coinpet.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.quadcoder.coinpet.logger.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Phangji on 3/27/15.
 */
public class BluetoothManager {
    private static final String TAG = "BluetoothManager";

    public String SERVICE_NAME = "COINPET-1234";
    static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private BluetoothAdapter mAdapter;
    private Handler mHandler;
    private ConnectThread mConnectThread;
    private ChatThread mConnectedThread;
    private int mState;

    // Constants that indicate the current connection state
    public static final int STATE_NONE = 0;       // we're doing nothing
    public static final int STATE_BT_ENABLED = 1;     // now listening for incoming connections
    public static final int STATE_CONNECTING = 2; // now initiating an outgoing connection
    public static final int STATE_CONNECTED = 3;  // now connected to a remote device
    public static final int STATE_LISTEN = 4;
    public static final int STATE_PAIRING = 5;
    public static final int STATE_DISCOVERING = 6;
//    public boolean isConnected = false;
//    public boolean btEnabled = true;
//    private static BluetoothService instance;
//    public static BluetoothService getInstance(Context context, Handler handler) {
////        if (instance == null)
////            instance = new BluetoothService(context, handler);
//        return new BluetoothService(context, handler);
//    }

    public BluetoothManager(Context context, Handler handler) {
        Log.d(TAG, "BluetoothService created ");
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        mState = STATE_NONE;
        mHandler = handler;
    }

    public synchronized void setState(int state) {
        Log.d(TAG, "setState() " + mState + " -> " + state);
        mState = state;

        // Give the new state to the Handler so the UI Activity can update
        mHandler.obtainMessage(Constants.MESSAGE_STATE_CHANGE, state, -1).sendToTarget();
    }

    public synchronized int getState() {
        return mState;
    }





    public synchronized BluetoothDevice searchPaired() {
        // 자동으로 찾아준다.
        BluetoothDevice searchedDevice = null;
        Set<BluetoothDevice> pairedDevice = mAdapter.getBondedDevices();
        if (pairedDevice.size() > 0) {
            for (BluetoothDevice device : pairedDevice) {
                if (device.getName().equals(SERVICE_NAME)) {
                    searchedDevice = device;
                    setState(STATE_PAIRING);
                    break;
                }
            }
        }
        return searchedDevice;
    }
    public synchronized void connect(BluetoothDevice device) {
        Log.d(TAG, "connect to: " + device);

        // Cancel any thread attempting to make a connection
        if (mState == STATE_CONNECTING) {
            if (mConnectThread != null) {
                mConnectThread.cancel();
                mConnectThread = null;
            }
        }

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        // Start the thread to connect with the given device
        mConnectThread = new ConnectThread(device);
        mConnectThread.start();
        setState(STATE_CONNECTING);
    }

    public synchronized void connected(BluetoothSocket socket, BluetoothDevice
            device) {
        Log.d(TAG, "ParedDevice connected");



        // Cancel the thread that completed the connection
        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        // Start the thread to manage the connection and perform transmissions
        mConnectedThread = new ChatThread(socket);
        mConnectedThread.start();

        // Send the name of the connected device back to the UI Activity
        Message msg = mHandler.obtainMessage(Constants.MESSAGE_DEVICE_NAME);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.DEVICE_NAME, device.getName());
        msg.setData(bundle);
        mHandler.sendMessage(msg);

        setState(STATE_CONNECTED);
    }

    public synchronized void start() {
        Log.d(TAG, "start");

        // Cancel any thread attempting to make a connection
        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        setState(STATE_LISTEN);
    }

    public synchronized void stop() {
        Log.d(TAG, "stop");

        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        setState(STATE_NONE);
    }

    public void write(byte[] out) {
        // Create temporary object
        ChatThread r;
        // Synchronize a copy of the ConnectedThread
        synchronized (this) {
            if (mState != STATE_CONNECTED) return;
            r = mConnectedThread;
        }
        // Perform the write unsynchronized
        r.write(out);
    }



    private class ConnectThread extends Thread {
        private BluetoothSocket mmSocket;
        BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            mmDevice = device;
            try {
                mmSocket = mmDevice.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "create() failed", e);
            }
        }

        @Override
        public void run() {
            try {
                mmSocket.connect();
                ChatThread chat = new ChatThread(mmSocket);
//                BluetoothService.getIntance().mChatList.add(chat);
//                chat.start();
                android.util.Log.d(TAG, "Bluetooth Connect Success");

            } catch (IOException e) {
                // Close the socket
                try {
                    mmSocket.close();
                } catch (IOException e2) {
                    Log.e(TAG, "unable to close() socket during connection failure", e2);
                }
                connectionFailed();
                return;
            }

            // Reset the ConnectThread because we're done
            synchronized (BluetoothManager.this) {
                mConnectThread = null;
            }

            // Start the connected thread
            connected(mmSocket, mmDevice);
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "close() of connect socket failed", e);
            }
        }
    }

    private class ChatThread extends Thread {
        BluetoothSocket mmSocket;
        InputStream mmInStream;
        OutputStream mmOutStream;

        public ChatThread(BluetoothSocket socket) {
            Log.d(TAG, "create ConnectedThread ");
            mmSocket = socket;

            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        @Override
        public void run() {
            byte[] buffer = new byte[1024]; // buffer store for the stream
            int bytes;  // bytes returned from read()
            byte[] data = null;
            // Keep listening to the InputStream until an exception occurs
            while(true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);
                    ByteBuffer wrapped = ByteBuffer.wrap(buffer);
                    data = wrapped.array(); //byte array

                    Log.d(TAG, "data : " + data[0] + " " + data[1] + " " + data[2] + " " + data[3] + " " + data[4] + " " + data[5]);

                    // Send the obtained bytes to the UI Activity
                    mHandler.obtainMessage(Constants.MESSAGE_READ, bytes, -1, buffer)
                            .sendToTarget();
                } catch (IOException e) {
                    e.printStackTrace();
                    try {
                        mmSocket.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    break;
                }
            }
//            BluetoothService.getIntance().mChatList.remove(this);
        }


        public void write(byte[] buffer) {
            try {
                mmOutStream.write(buffer);

                // Share the sent message back to the UI Activity
                mHandler.obtainMessage(Constants.MESSAGE_WRITE, -1, -1, buffer)
                        .sendToTarget();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "Exception during write", e);
//                try {
//                    mmSocket.close();
//                } catch (IOException e1) {
//                    e1.printStackTrace();
//                }
//                BluetoothService.getIntance().mChatList.remove(this);
            }
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "close() of connect socket failed", e);
            }
        }
    }

    private void connectionFailed() {
        // Send a failure message back to the Activity
        Message msg = mHandler.obtainMessage(Constants.MESSAGE_TOAST);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.TOAST, "Unable to connect device");
        msg.setData(bundle);
        mHandler.sendMessage(msg);

//        // Start the service over to restart listening mode
//        BluetoothService.this.start();
    }




}
