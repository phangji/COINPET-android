package com.quadcoder.coinpet.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Phangji on 3/27/15.
 */
public class BluetoothManager {
    private static final String TAG = "BluetoothManager";

    public String SERVICE_NAME = "COINPET-1234";    //TODO: PN번호 앞자리 잘라서 하는 걸로 추후 수정
    static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
//    static final UUID MY_UUID = UUID.fromString("00000000-0000-1000-8000-00805F9B34FB"); // 안되는 UUID

    private BluetoothAdapter mAdapter;
    private Handler mHandler;
    private ConnectThread mConnectThread;
    private ChatThread mConnectedThread;
    private int mState;



    private static class BluetoothManagerHolder {
        private static final BluetoothManager instance = new BluetoothManager();
    }

    public static BluetoothManager getInstance() {
        return BluetoothManagerHolder.instance;
    }

    private BluetoothManager() {
        Log.d(TAG, "BluetoothService created ");
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        mState = BTConstants.STATE_NONE;
        mHandler = new Handler();   //TODO:
    }

    public void setBtHandler(Handler handler) {
        mHandler = handler;
    }

    public Handler getBtHandler() {
        return mHandler;
    }

    public synchronized void setState(int state) {
        Log.d(TAG, "setState() " + mState + " -> " + state);
        mState = state;

        // Give the new state to the Handler so the UI Activity can update
        mHandler.obtainMessage(BTConstants.MESSAGE_STATE_CHANGE, state, -1).sendToTarget();
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
                    setState(BTConstants.STATE_PAIRING);
                    break;
                }
            }
        }
        return searchedDevice;
    }
    public synchronized void connect(BluetoothDevice device) {
        Log.d(TAG, "connect to: " + device);

        // Cancel any thread attempting to make a connection
        if (mState == BTConstants.STATE_CONNECTING) {
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
        setState(BTConstants.STATE_CONNECTING);
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
        Message msg = mHandler.obtainMessage(BTConstants.MESSAGE_DEVICE_NAME);
        Bundle bundle = new Bundle();
        bundle.putString(BTConstants.DEVICE_NAME, device.getName());
        msg.setData(bundle);
        mHandler.sendMessage(msg);

        setState(BTConstants.STATE_CONNECTED);
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

        setState(BTConstants.STATE_LISTEN);
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

        setState(BTConstants.STATE_NONE);
    }

    public void write(byte[] out) {
        // Create temporary object
        ChatThread r;
        // Synchronize a copy of the ConnectedThread
        synchronized (this) {
            if (mState != BTConstants.STATE_CONNECTED) return;
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
            byte[] buffer = new byte[1024]; // buffer store for the stream. It is not cleared after getting data.
            int bytes;  // bytes returned from read()
            // Keep listening to the InputStream until an exception occurs
            ArrayList<Byte> mOutBuffer = new ArrayList<>();
            while(true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);    //length
//                    Log.d(TAG, "MESSAGE_READ " + bytes + " bytes read");
                    int idx = 0;
                    while( idx < bytes) {
                        mOutBuffer.add(buffer[idx]);
                        if( buffer[idx] == BluetoothUtil.E) {
                            byte length = mOutBuffer.get(2);
                            // Send the obtained bytes to the UI Activity
                            mHandler.obtainMessage(BTConstants.MESSAGE_READ, length, -1, mOutBuffer.clone()).sendToTarget();

//                            for(int i=0; i < mOutBuffer.size(); i++) {
//                                Log.d(TAG, "phangji READ: " + mOutBuffer.get(i));
//                            }

                            mOutBuffer.clear();
                        }
                        idx++;
                    }

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
        }


        public void write(byte[] buffer) {
            try {
                Log.d(TAG, "MESSAGE_WRITE " + buffer.length + " bytes writen");
                for(int i=0; i<buffer.length; i++) {
                    Log.d(TAG, "phangji write " + buffer[i]);
                }
                mmOutStream.write(buffer);

                // Share the sent message back to the UI Activity
                mHandler.obtainMessage(BTConstants.MESSAGE_WRITE, -1, -1, buffer)
                        .sendToTarget();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "Exception during write", e);
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
        Message msg = mHandler.obtainMessage(BTConstants.MESSAGE_TOAST);
        Bundle bundle = new Bundle();
        bundle.putString(BTConstants.TOAST, "Unable to connect device");
        msg.setData(bundle);
        mHandler.sendMessage(msg);
    }
}
