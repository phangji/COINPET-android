package com.quadcoder.coinpet;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.quadcoder.coinpet.network.NetworkModel;
import com.quadcoder.coinpet.network.response.Res;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;


public class MainActivity extends ActionBarActivity {

    public final static String SERVICE_NAME = "COINPET-1234";
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    static final int REQUEST_ENABLE_BT = 1;
    BluetoothAdapter mBtAdapter;
    BluetoothDevice mDevice;
    boolean btEnabled = false;
    Handler mHandler = new Handler();
    boolean isConnected = false;

    ArrayList<ChatThread> mChatList;

    TextView textView;
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mChatList != null)
        for (int i = 0; i < mChatList.size(); i++) {
            ChatThread chat = mChatList.get(i);
            chat.closeSocket();
        }
        unregisterReceiver(mReceiver);
        NetworkModel.getInstance().cancelRequests(MainActivity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == RESULT_OK && requestCode == REQUEST_ENABLE_BT){
            btEnabled = true;
        } else if(requestCode == RESULT_CANCELED && requestCode == REQUEST_ENABLE_BT){
            btEnabled = false;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        textView = (TextView)findViewById(R.id.text);
        
        //Bluetooth 환경 설정
//        setBtEnvironment();
//
//        if(!PropertyManager.getInstance().isSet && btEnabled) { //등록 절차
//            initBtDevice();
//        }

        final char[] registerPn = new char[20];

        registerPn[0] = 'S';
        registerPn[1] = 0x01;
        registerPn[2] = 16;
        registerPn[19] = 'E';
        char[] pn = "1234123412341234".toCharArray();
        for(int i=3; i<19; i++) {
            registerPn[i] = pn[i-3];
        }
        final String send = new String(registerPn);
        Log.d("registerPn", send);


        Button btn = (Button)findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < mChatList.size(); i++) {
                    ChatThread chat = mChatList.get(i);
                    chat.write(send);
                }
                Log.d("mCharList", mChatList.size()+" 개");
            }
        });

        btn = (Button)findViewById(R.id.btnNet);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int money = 1200;

//                NetworkModel.getInstance().sendCoin(MainActivity.this, String.valueOf(money), new NetworkModel.OnNetworkResultListener<Res>() {
//                    @Override
//                    public void onResult(Res res) {
//                        if(res.result.equals("success")) {
//                            Toast.makeText(MainActivity.this, "Server Success", Toast.LENGTH_SHORT).show();
//                        } else {
//                            Toast.makeText(MainActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//                    @Override
//                    public void onFail(int code) {
//                        Toast.makeText(MainActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
//
//                    }
//                });

                NetworkModel.getInstance().signup(MainActivity.this, "test", 0, 9, new NetworkModel.OnNetworkResultListener<Res>() {
                    @Override
                    public void onResult(Res res) {
                        if(res.error == null) {
                            Toast.makeText(MainActivity.this, res.insertId + "" , Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, res.error, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFail(int code) {
                        Toast.makeText(MainActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public void initBtDevice(){
        mBtAdapter.startDiscovery();

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);
    }

    public void setBtEnvironment() {
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBtAdapter == null) {
            Toast.makeText(MainActivity.this, "Bluetooth not support", Toast.LENGTH_SHORT).show();
            finish();
        }

        if(!mBtAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            //블루투스를 안켜면..?? 통신에 관한 부분은 모두 DISABLED
        }else {
            btEnabled = true;
        }
    }

    class ConnectThread extends Thread {
        BluetoothDevice mDevice;

        public ConnectThread(BluetoothDevice device) {
            mDevice = device;
        }

        @Override
        public void run() {
            try {
                BluetoothSocket socket = mDevice.createRfcommSocketToServiceRecord(MY_UUID);
                socket.connect();
                ChatThread chat = new ChatThread(socket);
                mChatList.add(chat);
                chat.start();

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // 토스트도 여기 았어야함.
                        Toast.makeText(MainActivity.this, "Connect success", Toast.LENGTH_SHORT).show();
//                        mHandler.postDelayed(new Runnable() {
//
//                            @Override
//                            public void run() {
//                                for (int i = 0; i < mChatList.size(); i++) {
//                                    ChatThread chat = mChatList.get(i);
//                                    chat.write("l");
//                                }
//                            }
//                        }, 1000);
                    }
                });
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        Toast.makeText(MainActivity.this, "Connect fail", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        }
    }


    public void connectParedDevice() {  //맥주소로 하는 것으로 변경해야(나중에)

        // 자동으로 찾아준다.
        Set<BluetoothDevice> pairedDevice = mBtAdapter.getBondedDevices();
        if (pairedDevice.size() > 0) {
            for (BluetoothDevice device : pairedDevice) {
                if (device.getName().equals(SERVICE_NAME)) {
                    mDevice = device;
                    Toast.makeText(MainActivity.this, "Paired  "+ mDevice.getAddress(), Toast.LENGTH_SHORT).show();
                    isConnected = true;
                    break;
                }
            }
        }
    }



    // Create a BroadcastReceiver for ACTION_FOUND
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    // Get the BluetoothDevice object from the Intent
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                    //선택한 디바이스를 받아오면
                    if(device.getName() != null &&device.getName().equals(SERVICE_NAME)){
                        Toast.makeText(MainActivity.this, device.getName() + " discovered", Toast.LENGTH_SHORT).show();
                        mDevice = device;
                        mBtAdapter.cancelDiscovery();
                        isConnected = true;
                    }
            }

            // 찾은 이후
            if(isConnected) {
                mChatList = new ArrayList<ChatThread>();
                mHandler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        WrapBluetoothDevice device = new WrapBluetoothDevice(mDevice);
                        new ConnectThread(device.getDevice()).start();
                    }
                }, 2000);
            }

        }
    };



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class ChatThread extends Thread {
        BluetoothSocket mmSocket;
        InputStream mmInStream;
        OutputStream mmOutStream;
        public ChatThread(BluetoothSocket socket) {
            mmSocket = socket;

            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        @Override
        public void run() {
                // TODO Auto-generated method stub
                byte[] buffer = new byte[1024]; // buffer store for the stream
                int bytes;  // bytes returned from read()
//            char[] data = new char[10];
                byte[] data = null;
            int idx=0;
            // Keep listening to the InputStream until an exception occurs
            while(true) {   //하나씩 읽는 것 같음.
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);
                    ByteBuffer wrapped = ByteBuffer.wrap(buffer);
                    data = wrapped.array(); //byte array
//                    data[idx] = (char)num;
//                    idx++;

                    Log.d("data", " " + data[0] + " " + data[1] + " " + data[2] + " " + data[3]);


                    if(data != null && data[1] == 0x02) {
                        Log.d("test1", "0x02 success");
                        if(data[3] == 's') {
                            Log.d("test", "success");

                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() { //핸들러가 없거나 이게 없거나 --> 이거 확인해봐야함.

                                    mHandler.post(new Runnable() {

                                        @Override
                                        public void run() {
                                            Toast.makeText(MainActivity.this, "success", Toast.LENGTH_SHORT);
                                            textView.setText("success");
                                        }
                                    });
                                }
                            });

                        } else if(data[3] == 'f'){
//                            Toast.makeText(MainActivity.this, "success", Toast.LENGTH_SHORT);
                        }
                    }
                    if(data != null && data[1] == 0x08) {   // 동전입력 프로토콜
                        int money = data[3] * 256 * 256 + data[4] * 256 + data[5];
                        Toast.makeText(MainActivity.this, "" + money + " 원", Toast.LENGTH_SHORT).show();
                    }


                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    try {
                        mmSocket.close();
                    } catch (IOException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                    break;
                }

            }



            mChatList.remove(this);

        }


        public void write(String msg) {
            Log.d("msg", msg);
            try {
                mmOutStream.write(msg.getBytes());
                Log.i("write", "" + msg.getBytes());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                try {
                    mmSocket.close();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                mChatList.remove(this);
            }
        }

        public void closeSocket() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
