package com.quadcoder.coinpet.page.quiz;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.quadcoder.coinpet.page.common.MyApplication;
import com.quadcoder.coinpet.page.common.PropertyManager;
import com.quadcoder.coinpet.R;
import com.quadcoder.coinpet.bluetooth.BTConstants;
import com.quadcoder.coinpet.bluetooth.BluetoothManager;
import com.quadcoder.coinpet.bluetooth.BluetoothUtil;
import com.quadcoder.coinpet.database.DBManager;
import com.quadcoder.coinpet.logger.Log;
import com.quadcoder.coinpet.model.Quiz;
import com.quadcoder.coinpet.network.NetworkManager;
import com.quadcoder.coinpet.network.response.Res;
import com.quadcoder.coinpet.page.common.DialogActivity;
import com.quadcoder.coinpet.page.common.Utils;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuizFragment extends Fragment {

    private static final String TAG = "QuizFragment";

    public QuizFragment() {
        // Required empty public constructor
    }

    /**
     * layout elements
     * */

    TextView tvDiff;
    ImageView imgvQCoin;
    TextView tvTime;
    TextView tvQuiz;
    TextView tvHint;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_quiz, container, false);
        setMainLayout(rootView);

        return rootView;
    }

    private void setMainLayout(View rootView) {
        tvDiff = (TextView) rootView.findViewById(R.id.tvDiff);
        imgvQCoin = (ImageView) rootView.findViewById(R.id.imgvQCoin);
        tvTime = (TextView) rootView.findViewById(R.id.tvTime);
        tvQuiz = (TextView) rootView.findViewById(R.id.tvQuiz);
        tvHint = (TextView) rootView.findViewById(R.id.tvHint);

        imgvQCoin.setImageResource(Utils.getInstance().getQCoinResource(PropertyManager.getInstance().getqCoin()));


    }

    Quiz mQuiz;
    boolean isResult;

    private void settingQuiz() {
        // 플래그 false로 초기화
        isResult = false;

        // connection check : 다이얼로그 액티비티 실행 중 disconnect 된 것 감지
        mChatService.write(BluetoothUtil.getInstance().requestBoardConn());
        int coinCount = PropertyManager.getInstance().getqCoin();
        imgvQCoin.setImageResource(Utils.getInstance().getQCoinResource(coinCount));

        if (coinCount < 1) {
            isResult = true;
            Intent i = new Intent(getActivity(), DialogActivity.class);
            i.putExtra(DialogActivity.DIALOG_TYPE, DialogActivity.COINOVER);
            startActivityForResult(i, REQUEST_CODE_TIMEOVER);
            return;
        }



        // 데이터베이스에서 퀴즈 데이터 불러와 뷰 세팅
        mQuiz = DBManager.getInstance().getQuizRandom();
        tvQuiz.setText(mQuiz.content);
        tvDiff.setText(Utils.getInstance().getDiffResource(mQuiz.level));
        tvTime.setText(mQuiz.time + "");
        tvHint.setText(R.string.show_hint);
        tvHint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvHint.setText(mQuiz.hint);  //누를 때마다 set
            }
        });

        mTimer = new CountDownTimer(mQuiz.time * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvTime.setText("" + millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                if(!isResult) {
                    mChatService.write(BluetoothUtil.getInstance().quizTimeOver());
                    mQuiz.state = Quiz.STATE_WRONG;
                    afterQuizUpdateData();

                    // 문제 동전 삭감
                    PropertyManager.getInstance().minusQCoin();

                    Intent i = new Intent(getActivity(), DialogActivity.class);
                    i.putExtra(DialogActivity.DIALOG_TYPE, DialogActivity.TIMEOVER);
                    i.putExtra(DialogActivity.TEXT, mQuiz.solution);
                    startActivityForResult(i, REQUEST_CODE_TIMEOVER);
                }
                isResult = true;
            }
        };
    }

    private BluetoothManager mChatService = null;

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

        settingQuiz();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mChatService.write(BluetoothUtil.getInstance().quizIsEnded());
        //TODO : ack 와 연결

        beforeFinishing();
    }

    void beforeFinishing() {
//        if( mChatService != null)
//            mChatService.stop();
        NetworkManager.getInstance().cancelRequests(getActivity());
        if(mTimer != null) {
            mTimer.cancel(); // cancel 했는데..
        }
    }

    CountDownTimer mTimer;

    @Override
    public void onResume() {
        super.onResume();
        //timer 시작
        mTimer.start();
    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BTConstants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BTConstants.STATE_CONNECTED:
                            Toast.makeText(MyApplication.getContext(), TAG + "/ state connected", Toast.LENGTH_SHORT).show();
                            break;
                        case BTConstants.STATE_CONNECTING:
                            Toast.makeText(MyApplication.getContext(), TAG + "/ state connecting", Toast.LENGTH_SHORT).show();
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
//                    Toast.makeText(MyApplication.getContext(), TAG + " / Me : " + writeMessage, Toast.LENGTH_SHORT).show();
                    break;

                case BTConstants.MESSAGE_READ :

                    ArrayList<Byte> readBuffer = (ArrayList<Byte>) msg.obj;
                    Toast.makeText(getActivity(), "Tutorial / Device : " + readBuffer.toString(), Toast.LENGTH_SHORT).show();
                    int length = msg.arg1;
                    android.util.Log.d(TAG, "MESSAGE_READ " + length + " bytes read");
                    byte opcode = readBuffer.get(1);

                    if(opcode == BluetoothUtil.Opcode.QUIZ_USER_INPUT) {
                        int[] num = new int[3];
                        for(int i=3; i<=5; i++) {
                            num[i-3] = readBuffer.get(i);
                            if(num[i-3] < 0) {
                                num[i-3] += 256;
                            }
                        }
                        Log.d("TAG", num[0] + " " + num[1] + " " + num[2] + " ");
                        int money = num[0] * 256 * 256 + num[1] * 256 + num[2];
                        checkUserInput(money);
                    }

                    if(opcode == BluetoothUtil.Opcode.BOARD_CON_RES) {
                        if( readBuffer.get(3) == BluetoothUtil.YES ) {
                            Toast.makeText(getActivity(), "BOARD CONN YES", Toast.LENGTH_SHORT).show();

                        } else if ( readBuffer.get(3) == BluetoothUtil.NO ){
                            Toast.makeText(getActivity(), "BOARD CONN NO", Toast.LENGTH_SHORT).show();
                            beforeFinishing();
                            goReadyPage();
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

                    if(opcode == BluetoothUtil.Opcode.QUIZ_DISCONN) {
                        beforeFinishing();
                        goReadyPage();
                    }

                    if(opcode == BluetoothUtil.Opcode.ACK) {
                        Toast.makeText(getActivity(), TAG + "/ ACK", Toast.LENGTH_SHORT).show();
                    }
                    break;

            }
        }
    };



//    void sendEventToMain() {
//        // 이벤트들을 intent에 담아서, 이 액티비티가 task에서 빠지고, main이 나올 때 main에게 전해준다.
//        if(moneyList.size() > 0) {
//            Intent result = new Intent();
//            result.putExtra(INTENT_MONEY, moneyList);
//            getActivity().setResult(Activity.RESULT_OK, result);
//            getActivity().finish();
//        } else {
//            getActivity().finish();
//        }
//    }

    private static final int REQUEST_CODE_RIGHT = 0;
    private static final int REQUEST_CODE_WRONG = 1;
    private static final int REQUEST_CODE_TIMEOVER = 2;
    private static final int REQUEST_CODE_COINOVER = 3;


    void checkUserInput(int userValue) {
        mTimer.cancel();

        if (!isResult) {
            if( isRight(userValue) ) {
                mQuiz.state = Quiz.STATE_CORRECT;
                afterQuizUpdateData();
                Intent i = new Intent(getActivity(), DialogActivity.class);
                i.putExtra(DialogActivity.DIALOG_TYPE, DialogActivity.RIGHT);
                i.putExtra(DialogActivity.TEXT, mQuiz.solution);
                startActivityForResult(i, REQUEST_CODE_RIGHT);
            } else {
                mQuiz.state = Quiz.STATE_WRONG;
                afterQuizUpdateData();

                // 문제 동전 삭감
                PropertyManager.getInstance().minusQCoin();

                Intent i = new Intent(getActivity(), DialogActivity.class);
                i.putExtra(DialogActivity.DIALOG_TYPE, DialogActivity.WRONG);
                i.putExtra(DialogActivity.TEXT, mQuiz.solution);
                startActivityForResult(i, REQUEST_CODE_WRONG);
            }
        }
        isResult = true;
    }


    private void goReadyPage() {
        if (!isResult) {
            ((QuizActivity)getActivity()).setPageIndex(0);
            FragmentManager fm = getActivity().getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragment, new QuizReadyFragment());
            ft.addToBackStack(null);
            ft.commit();
        }
    }

    boolean isRight(int userValue) {
        return userValue == mQuiz.answer;
    }


    void afterQuizUpdateData() {
        // TODO: 주석 풀어야 맞는 건데, 일단 안해놓음.
//        NetworkManager.getInstance().postQuiz(getActivity(), mQuiz.pk_std_quiz, mQuiz.state, new NetworkManager.OnNetworkResultListener<Res>() {
//            @Override
//            public void onResult(Res res) {
//                DBManager.getInstance().updateQuiz(mQuiz);
//            }
//
//            @Override
//            public void onFail(Res res) {
//
//            }
//        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_RIGHT :
                case REQUEST_CODE_WRONG :
                case REQUEST_CODE_TIMEOVER :
                    boolean wantsNextQuiz = data.getBooleanExtra(DialogActivity.RESULT_NEXT_QUIZ, false);
                    if(wantsNextQuiz) {
                        settingQuiz();
                    } else {
                        getActivity().finish();
                    }
                    break;
                case REQUEST_CODE_COINOVER :
                    getActivity().finish();
                    break;
            }
        }
    }


}
