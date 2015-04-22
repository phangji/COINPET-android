package com.quadcoder.coinpet.page.tutorial;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.quadcoder.coinpet.MainActivity;
import com.quadcoder.coinpet.PropertyManager;
import com.quadcoder.coinpet.R;
import com.quadcoder.coinpet.bluetooth.BluetoothService;
import com.quadcoder.coinpet.logger.Log;
import com.quadcoder.coinpet.network.NetworkModel;
import com.quadcoder.coinpet.page.common.Constants;
import com.quadcoder.coinpet.page.signup.SignupActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class TutorialThirdFragment extends Fragment {


    private final String TAG = "TutorialThirdFragment";
    public TutorialThirdFragment() {
        // Required empty public constructor
    }
    static final int REQUEST_ENABLE_BT = 1;

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
            if (mChatService.getState() == BluetoothService.STATE_NONE) {
                // Start the Bluetooth chat services
                mChatService.start();
            }
        }

        Button btn = (Button)rootView.findViewById(R.id.btnNext);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePnPsg();
                mChatService.write(pnMsg.getBytes());
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(getActivity(), SignupActivity.class));
                        getActivity().finish();
                    }
                }, 2000);
            }
        });

        return rootView;
    }

    String pnMsg = null;

    void makePnPsg() {
        final char[] registerPn = new char[20];
        registerPn[0] = 'S';
        registerPn[1] = 0x01;
        registerPn[2] = 16;
        registerPn[19] = 'E';
        char[] pn = PropertyManager.getInstance().getPn().toCharArray();
        for(int i=3; i<19; i++) {
            registerPn[i] = pn[i-3];
        }
        pnMsg = new String(registerPn);
        Log.d("registerPn", pnMsg);
    }

    BluetoothService mChatService;
    Handler mHandler = new Handler();
    StringBuffer mOutStringBuffer;
    private void setupChatService() {
        Log.d(TAG, "setupChatService()");

        // Initialize the BluetoothChatService to perform bluetooth connections
        mChatService = new BluetoothService(getActivity(), mHandler);

        // Initialize the buffer for outgoing messages
        mOutStringBuffer = new StringBuffer("");
    }

    @Override
    public void onStart() {
        super.onStart();
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
            mChatService.setState(BluetoothService.STATE_BT_ENABLED);
            connectBt();
        }
        else {
            mChatService.setState(BluetoothService.STATE_BT_ENABLED);
            connectBt();
        }
    }

    boolean isRegistered = false;

    public void discovery(){
        mBtAdapter.startDiscovery();
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        getActivity().registerReceiver(mReceiver, filter);
        isRegistered = true;
    }
    BluetoothDevice mDevice;
    void connectBt() {
        if(mChatService.getState() == BluetoothService.STATE_BT_ENABLED) {
            mDevice = mChatService.searchPaired();

            if (mDevice == null) {  //페어링된 적이 없다면,
                discovery();
            }
            mChatService.connect(mDevice);
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
                    mChatService.setState(BluetoothService.STATE_DISCOVERING);
                }
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mChatService != null) {
            mChatService.stop();
        }

        if(isRegistered)
            getActivity().unregisterReceiver(mReceiver);
    }
}
