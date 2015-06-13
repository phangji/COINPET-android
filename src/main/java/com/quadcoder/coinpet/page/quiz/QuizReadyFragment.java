package com.quadcoder.coinpet.page.quiz;

import android.bluetooth.BluetoothAdapter;
import android.graphics.drawable.AnimationDrawable;
import android.os.CountDownTimer;
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
import android.widget.Toast;

import com.quadcoder.coinpet.R;
import com.quadcoder.coinpet.bluetooth.BTConstants;
import com.quadcoder.coinpet.bluetooth.BluetoothManager;
import com.quadcoder.coinpet.bluetooth.BluetoothUtil;
import com.quadcoder.coinpet.logger.Log;
import com.quadcoder.coinpet.page.common.MyApplication;

import java.util.ArrayList;

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

        rootView.findViewById(R.id.btnRetry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mChatService.write(BluetoothUtil.getInstance().requestBoardConn());
            }
        });

        mTimer = new CountDownTimer(120 * 1000, 10 * 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mChatService.write(BluetoothUtil.getInstance().requestBoardConn());
            }

            @Override
            public void onFinish() {
                Toast.makeText(getActivity(), "연결이 되지 않아 퀴즈를 종료합니다.", Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        };

        return rootView;
    }


    @Override
    public void onStart() {
        super.onStart();

        if (mChatService != null) {
            mChatService.connectDevice();
        } else {
            mChatService = BluetoothManager.getInstance();
            mChatService.setBtHandler(mHandler);
        }

        ((AnimationDrawable)imgvPad.getDrawable()).start();
    }

    CountDownTimer mTimer;

    @Override
    public void onResume() {
        super.onResume();
        if( ((QuizActivity)getActivity()).getPageIndex() == 0) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mTimer.start();
                }
            }, 2000);

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTimer.cancel();
//        if (mChatService != null) {
//            mChatService.stop();
//        }
    }

    private void goNext() {
        mTimer.cancel();
        ((QuizActivity)getActivity()).setPageIndex(1);
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
                            Toast.makeText(MyApplication.getContext(), TAG + "/ state none", Toast.LENGTH_SHORT).show();
                            break;
                    }
                    break;
                case BTConstants.MESSAGE_WRITE:
//                    byte[] writeBuf = (byte[]) msg.obj;
//                    String writeMessage = new String(writeBuf);
//                    Toast.makeText(getActivity(), TAG + " / Me : " + writeMessage, Toast.LENGTH_SHORT).show();
                    break;

                case BTConstants.MESSAGE_READ :

                    ArrayList<Byte> readBuffer = (ArrayList<Byte>) msg.obj;
                    Toast.makeText(getActivity(), "Device : " + readBuffer.toString(), Toast.LENGTH_SHORT).show();
                    int length = msg.arg1;
                    android.util.Log.d(TAG, "MESSAGE_READ " + length + " bytes read");
                    byte opcode = readBuffer.get(1);

                    if(opcode == BluetoothUtil.Opcode.BOARD_CON_RES) {
                        if( readBuffer.get(3) == BluetoothUtil.YES ) {
                            Toast.makeText(getActivity(), "BOARD CONN YES", Toast.LENGTH_SHORT).show();
                            goNext();
                        } else if ( readBuffer.get(3) == BluetoothUtil.NO ){
                            Toast.makeText(getActivity(), "BOARD CONN NO", Toast.LENGTH_SHORT).show();
                        }
                    }

                    if(opcode == BluetoothUtil.Opcode.READ_MONEY) {
                        int[] num = new int[3];
                        for(int i=3; i<=5; i++) {
                            num[i-3] = readBuffer.get(i);
                            if(num[i-3] < 0) {
                                num[i-3] += 256;
                            }
                        }
                        final int money = num[0] * 256 * 256 + num[1] * 256 + num[2];

                        ((QuizActivity)getActivity()).moneyList.add( new Integer(money) );

                    }

                    break;

            }
        }
    };

    private BluetoothManager mChatService = null;

    @Override
    public void onDetach() {
        super.onDetach();

    }
}
