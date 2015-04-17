package com.quadcoder.coinpet;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.quadcoder.coinpet.bluetooth.BluetoothService;
import com.quadcoder.coinpet.bluetooth.Constants;
import com.quadcoder.coinpet.logger.Log;
import com.quadcoder.coinpet.logger.LogWrapper;
import com.quadcoder.coinpet.network.NetworkModel;
import com.quadcoder.coinpet.network.response.Cost;
import com.quadcoder.coinpet.page.mypet.MyPetActivity;
import com.quadcoder.coinpet.page.tutorial.TutorialActivity;


public class MainActivity extends ActionBarActivity {

    static final int REQUEST_ENABLE_BT = 1;
    BluetoothDevice mDevice;
    /**
     * Local Bluetooth adapter
     */
    private BluetoothAdapter mBtAdapter = null;

    /**
     * Member object for the chat services
     */
    private BluetoothService mChatService = null;

    TextView textView;
    private StringBuffer mOutStringBuffer;

    @Override
    protected void onStart() {
        super.onStart();
        initializeLogging();
    }

    public static final String TAG = "MainActivity";

    /** Set up targets to receive log data */
    public void initializeLogging() {
        // Using Log, front-end to the logging chain, emulates android.util.log method signatures.
        // Wraps Android's native log framework
        LogWrapper logWrapper = new LogWrapper();
        Log.setLogNode(logWrapper);

        Log.i(TAG, "Ready");
    }

    boolean isRegistered = false;

    public void discovery(){
        mBtAdapter.startDiscovery();
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);
        isRegistered = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        if(mChatList != null)
//        for (int i = 0; i < mChatList.size(); i++) {
//            ChatThread chat = mChatList.get(i);
//            chat.closeSocket();
//        }
        if (mChatService != null) {
            mChatService.stop();
        }

        if(isRegistered)
            unregisterReceiver(mReceiver);
        NetworkModel.getInstance().cancelRequests(MainActivity.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        if (mChatService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (mChatService.getState() == BluetoothService.STATE_NONE) {
                // Start the Bluetooth chat services
                mChatService.start();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { //블루투스 연결 권장 다이얼로그 호출 결과
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                if (resultCode == Activity.RESULT_OK) {
                    setupChatService();
                    mChatService.setState(BluetoothService.STATE_BT_ENABLED);
                    connectBt();


                } else if (requestCode == RESULT_CANCELED) {
                    Log.d(TAG, "BT not enabled");
                    mChatService.setState(BluetoothService.STATE_NONE);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    String pnMsg = null;
    void makePnPsg() {
        final char[] registerPn = new char[20];
        registerPn[0] = 'S';
        registerPn[1] = 0x01;
        registerPn[2] = 16;
        registerPn[19] = 'E';
        char[] pn = "1234123412341234".toCharArray();
        for(int i=3; i<19; i++) {
            registerPn[i] = pn[i-3];
        }
        pnMsg = new String(registerPn);
        Log.d("registerPn", pnMsg);
    }

    void connectBt() {
        if(mChatService.getState() == BluetoothService.STATE_BT_ENABLED) {
            mDevice = mChatService.searchPaired();

            if (mDevice != null) {  //페어링된 적이 없다면,
                discovery();

            }
            mChatService.connect(mDevice);
        }



    }

    void setMainLayout() {

        // MyPet
        ImageView mainBtn = (ImageView)findViewById(R.id.imgvMyPet);
        mainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MyPetActivity.class));
            }
        });

        // Cashbook
        mainBtn = (ImageView)findViewById(R.id.imgvCashbook);
        mainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // Quest
        mainBtn = (ImageView)findViewById(R.id.imgvQuest);
        mainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // Reward
        mainBtn = (ImageView)findViewById(R.id.imgvReward);
        mainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // Notification
        mainBtn = (ImageView)findViewById(R.id.imgvNoti);
        mainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // Setting
        mainBtn = (ImageView)findViewById(R.id.imgvSetting);
        mainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //임시로 Tutorial 실행
                startActivity(new Intent(MainActivity.this, TutorialActivity.class));
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView)findViewById(R.id.text);

        setMainLayout();

//        setBtEnvironment();

        Button btn = (Button)findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                for (int i = 0; i < mChatList.size(); i++) {
//                    ChatThread chat = mChatList.get(i);
//                    chat.write(send);
//                }
//                Log.d("mCharList", mChatList.size()+" 개");
                makePnPsg();
                mChatService.write(pnMsg.getBytes());
            }
        });

        btn = (Button)findViewById(R.id.btnNet);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NetworkModel.getInstance().sendCoin(MainActivity.this, 12340, new NetworkModel.OnNetworkResultListener<Cost>() {
                    @Override
                    public void onResult(Cost res) {
                        if(res.error == null) {
                            Toast.makeText(MainActivity.this, "Server Success", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFail(int code) {
                        Toast.makeText(MainActivity.this, "Server Error " + code, Toast.LENGTH_SHORT).show();

                    }
                });


            }
        });
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
                    if(device.getName() != null &&device.getName().equals(mChatService.SERVICE_NAME)){
                        Toast.makeText(MainActivity.this, device.getName() + " discovered", Toast.LENGTH_SHORT).show();
                        mDevice = device;
                        mBtAdapter.cancelDiscovery();
                        mChatService.setState(BluetoothService.STATE_DISCOVERING);
                    }
            }
        }
    };

//    void createChatThread(boolean isConnected) {
//        // 찾은 이후
//        if(isConnected) {
//            mChatList = new ArrayList<ChatThread>();
//            mHandler.postDelayed(new Runnable() {
//
//                @Override
//                public void run() {
//                    WrapBluetoothDevice device = new WrapBluetoothDevice(mDevice);
//                    new ConnectThread(device.getDevice()).start();
//                }
//            }, 2000);
//        }
//    }

    public void setBtEnvironment() {
        //Bluetooth 환경 설정
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBtAdapter == null) {
            Toast.makeText(MainActivity.this, "블루투스를 지원하지 않는 휴대폰입니다.", Toast.LENGTH_SHORT).show();
            finish();
        }
        if(!mBtAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else if(mChatService == null) {
            setupChatService();
            mChatService.setState(BluetoothService.STATE_BT_ENABLED);
            connectBt();
        }
        else {
            mChatService.setState(BluetoothService.STATE_BT_ENABLED);
            connectBt();
        }
    }

    private void setupChatService() {
        Log.d(TAG, "setupChatService()");

        // Initialize the BluetoothChatService to perform bluetooth connections
        mChatService = new BluetoothService(MainActivity.this, mHandler);

        // Initialize the buffer for outgoing messages
        mOutStringBuffer = new StringBuffer("");
    }

    /**
     * The Handler that gets information back from the BluetoothChatService
     */
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:
                            Toast.makeText(MainActivity.this, "state connected", Toast.LENGTH_SHORT).show();
                            break;
                        case BluetoothService.STATE_CONNECTING:
                            Toast.makeText(MainActivity.this, "state connecting", Toast.LENGTH_SHORT).show();
                            break;
                        case BluetoothService.STATE_LISTEN:
                        case BluetoothService.STATE_NONE:
                            Toast.makeText(MainActivity.this, "state none", Toast.LENGTH_SHORT).show();
                            break;
                    }
                    break;
                case Constants.MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
                    Toast.makeText(MainActivity.this, "Me : " + writeMessage, Toast.LENGTH_SHORT).show();
                    break;
                case Constants.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    Toast.makeText(MainActivity.this, "Device : " + readMessage, Toast.LENGTH_SHORT).show();
                    break;
                case Constants.MESSAGE_DEVICE_NAME:
                    String deviceName = msg.getData().getString(Constants.DEVICE_NAME);
                    Toast.makeText(MainActivity.this, "Connected to "
                                + deviceName, Toast.LENGTH_SHORT).show();
                    break;
                case Constants.MESSAGE_TOAST:
                    Toast.makeText(MainActivity.this, msg.getData().getString(Constants.TOAST),
                                Toast.LENGTH_SHORT).show();

                    break;
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
}
