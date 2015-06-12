package com.quadcoder.coinpet.page.tutorial;


import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.quadcoder.coinpet.page.common.PropertyManager;
import com.quadcoder.coinpet.R;
import com.quadcoder.coinpet.bluetooth.BTConstants;
import com.quadcoder.coinpet.bluetooth.BluetoothManager;
import com.quadcoder.coinpet.bluetooth.BluetoothUtil;
import com.quadcoder.coinpet.page.common.Constants;
import com.quadcoder.coinpet.page.story.StoryActivity;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class Tutorial3Fragment extends Fragment {


    private final String TAG = "TutorialThirdFragment";
    public Tutorial3Fragment() {
        // Required empty public constructor
    }
    static final int REQUEST_ENABLE_BT = 1;

    Button btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tutorial_third, container, false);

        TextView txt = (TextView) rootView.findViewById(R.id.tvGuide);
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), Constants.FONT_NORMAL);
        txt.setTypeface(font);
        setBtEnvironment();

//        BluetoothService.getInstance(getActivity(), new Handler()).write(pnMsg.getBytes());

        if (mChatService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (mChatService.getState() == BTConstants.STATE_NONE) {
                // Start the Bluetooth chat services
                mChatService.start();
            }
        }

        btn = (Button)rootView.findViewById(R.id.btnNext);
        btn.setVisibility(View.INVISIBLE);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mChatService.write(BluetoothUtil.getInstance().registerPn());

            }
        });

        return rootView;
    }

    void moveToNextPage() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getActivity(), StoryActivity.class));
                getActivity().finish();
            }
        }, 2000);
    }

    BluetoothManager mChatService;
    boolean utcIsSent;
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BTConstants.MESSAGE_STATE_CHANGE:
                    int state = msg.arg1;
                    if(state == BTConstants.STATE_CONNECTED) {
                        btn.setVisibility(View.VISIBLE);
                    }
                    break;
                case BTConstants.MESSAGE_READ:
                    btn.setVisibility(View.VISIBLE);
                    ArrayList<Byte> readBuffer = (ArrayList<Byte>) msg.obj;
                    Toast.makeText(getActivity(), "Tutorial / Device : " + readBuffer.toString(), Toast.LENGTH_SHORT).show();
                    int length = msg.arg1;
                    android.util.Log.d(TAG, "MESSAGE_READ " + length + " bytes read");
                    byte opcode = readBuffer.get(1);

                    if(opcode == BluetoothUtil.Opcode.PN_RESPONSE) {
                        Toast.makeText(getActivity(), "PN RESPONSE " + readBuffer.get(3), Toast.LENGTH_SHORT).show();
                        if(readBuffer.get(3) == BluetoothUtil.SUCCESS) {
                            mChatService.write(BluetoothUtil.getInstance().sendUTC());
                            utcIsSent = true;
                        } else {
                            mChatService.write(BluetoothUtil.getInstance().registerPn());
                        }
                    }

                    if(opcode == BluetoothUtil.Opcode.ACK) {
                        Toast.makeText(getActivity(), "ACK " + readBuffer.get(3), Toast.LENGTH_SHORT).show();
                        if(readBuffer.get(3) == BluetoothUtil.SUCCESS && utcIsSent) {
                            moveToNextPage();
                        } else {
                            Toast.makeText(getActivity(), "last ACK fail", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
            }
        }
    };

    private void setupChatService() {
        Log.d(TAG, "setupChatService()");

        // Initialize the BluetoothChatService to perform bluetooth connections
        mChatService = BluetoothManager.getInstance();
        mChatService.setBtHandler(mHandler);

        // Initialize the buffer for outgoing messages
    }

    @Override
    public void onStart() {
        super.onStart();

        if (mChatService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (mChatService.getState() == BTConstants.STATE_NONE) {
                // Start the Bluetooth chat services
                mChatService.start();
            } 
        }
    }

    BluetoothAdapter mBtAdapter;
    public void setBtEnvironment() {
        //Bluetooth 환경 설정
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBtAdapter == null) {
            Toast.makeText(getActivity(), "블루투스를 지원하지 않는 휴대폰입니다.", Toast.LENGTH_SHORT).show();
            getActivity().finish();
        }
        if(!mBtAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else if(mChatService == null) {
            setupChatService();
            mChatService.setState(BTConstants.STATE_BT_ENABLED);
            connectBt();
        }
        else {
            mChatService.setState(BTConstants.STATE_BT_ENABLED);
            connectBt();
        }
    }

    boolean isRegistered = false;

    public void discovery(){
        mBtAdapter.startDiscovery();
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        getActivity().registerReceiver(mReceiver, filter);

    }

    BluetoothDevice mDevice;

    void connectBt() {
        if(mChatService.getState() == BTConstants.STATE_BT_ENABLED) {
            mDevice = mChatService.searchPaired();

            if( mDevice != null) {
                mChatService.connect(mDevice);
            } else {  //페어링된 적이 없다면,
                discovery();
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
                if(device.getName() != null &&device.getName().equals(mChatService.SERVICE_NAME)){
                    Toast.makeText(getActivity(), device.getName() + " discovered", Toast.LENGTH_SHORT).show();
                    mDevice = device;
                    mBtAdapter.cancelDiscovery();
                    mChatService.setState(BTConstants.STATE_DISCOVERING);
                    mChatService.connect(mDevice);
                    isRegistered = true;
                }
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) { //블루투스 연결 권장 다이얼로그 호출 결과
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                if (resultCode == Activity.RESULT_OK) {
                    setupChatService();
                    mChatService.setState(BTConstants.STATE_BT_ENABLED);
                    connectBt();
                    Log.d("phangji bt", "bt enabled result ok");

                } else if (requestCode == Activity.RESULT_CANCELED) {
                    PropertyManager.getInstance().setBtRequested(false);
                    Log.d("phangji bt", "bt enabled canceled");
                } else {    //블루투스를 켜시겠습니까? 아니요. OR 취소
                    PropertyManager.getInstance().setBtRequested(false);
                    Log.d("phangji bt", "BT not enabled");
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        if (mChatService != null) {
//            mChatService.stop();
//        }

        if(isRegistered)
            getActivity().unregisterReceiver(mReceiver);
    }

}
