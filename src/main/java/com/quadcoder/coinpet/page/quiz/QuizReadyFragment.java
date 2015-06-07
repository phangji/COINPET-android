package com.quadcoder.coinpet.page.quiz;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.quadcoder.coinpet.MainActivity;
import com.quadcoder.coinpet.R;
import com.quadcoder.coinpet.bluetooth.BTConstants;
import com.quadcoder.coinpet.bluetooth.BluetoothManager;
import com.quadcoder.coinpet.bluetooth.BluetoothUtil;
import com.quadcoder.coinpet.logger.Log;

/**
 * A placeholder fragment containing a simple view.
 */
public class QuizReadyFragment extends Fragment {

    public static final String TAG = "QuizReadyFragment";

    public QuizReadyFragment() {
    }
    ImageView imgvPad;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_quiz_ready, container, false);

        imgvPad = (ImageView) rootView.findViewById(R.id.imgvPad);

        if( !BluetoothAdapter.getDefaultAdapter().isEnabled() ) {
            Toast.makeText(getActivity(), "블루투스가 켜져 있지 않습니다.", Toast.LENGTH_SHORT).show();
            getActivity().finish();
        }


        return rootView;
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
        } else {
            mChatService = BluetoothManager.getInstance();
            mChatService.setBtHandler(mHandler);
        }

        ((AnimationDrawable)imgvPad.getDrawable()).start();
    }

    @Override
    public void onResume() {
        super.onResume();
        // check board
        mChatService.write(BluetoothUtil.getInstance().requestBoardConn());

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mChatService != null) {
            mChatService.stop();
        }
    }

    private void goNext() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment, new QuizFragment());
        ft.addToBackStack(null);
        ft.commit();
    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BTConstants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BTConstants.STATE_CONNECTED:
                            Toast.makeText(getActivity(), TAG + "/ state connected", Toast.LENGTH_SHORT).show();
                            break;
                        case BTConstants.STATE_CONNECTING:
                            Toast.makeText(getActivity(), TAG + "/ state connecting", Toast.LENGTH_SHORT).show();
                            break;
                        case BTConstants.STATE_LISTEN:
                        case BTConstants.STATE_NONE:
                            Toast.makeText(getActivity(), TAG + "/ state none", Toast.LENGTH_SHORT).show();
                            break;
                    }
                    break;
                case BTConstants.MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    String writeMessage = new String(writeBuf);
                    Toast.makeText(getActivity(), TAG + " / Me : " + writeMessage, Toast.LENGTH_SHORT).show();
                    break;

                case BTConstants.MESSAGE_READ :
                    byte[] readBuf = (byte[]) msg.obj;
                    if(readBuf != null && readBuf[1] == BluetoothUtil.Opcode.BOARD_CON_RES) {
                        if( readBuf[3] == BluetoothUtil.DUMM ) {
                            Toast.makeText(getActivity(), "BOARD CONN SUCCESS", Toast.LENGTH_SHORT).show();
                            mChatService.write(BluetoothUtil.getInstance().ack(true));
                            goNext();
                        } else {
                            Toast.makeText(getActivity(), "BOARD CONN FAIL", Toast.LENGTH_SHORT).show();
                            mChatService.write(BluetoothUtil.getInstance().ack(false));
                        }
                    }

                    break;

            }
        }
    };

    private BluetoothManager mChatService = null;


    private void setupChatService() {
        Log.d(TAG, "setupChatService()");

        // Initialize the BluetoothChatService to perform bluetooth connections
        mChatService = BluetoothManager.getInstance();
        mChatService.setBtHandler(mHandler);
    }
}
