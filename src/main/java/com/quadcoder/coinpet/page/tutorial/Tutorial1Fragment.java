package com.quadcoder.coinpet.page.tutorial;


import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.quadcoder.coinpet.page.common.PropertyManager;
import com.quadcoder.coinpet.R;
import com.quadcoder.coinpet.network.NetworkManager;
import com.quadcoder.coinpet.network.response.Res;
import com.quadcoder.coinpet.page.common.Constants;
import com.quadcoder.coinpet.page.main.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class Tutorial1Fragment extends Fragment {


    public Tutorial1Fragment() {
        // Required empty public constructor
    }

    EditText etPn;
    private static final String TAG = "TutorialFirstFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tutorial_first, container, false);

        etPn = (EditText)rootView.findViewById(R.id.etPN);
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), Constants.FONT_NORMAL);
        etPn.setTypeface(font);

        Button btn = (Button)rootView.findViewById(R.id.btnNext);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String pn = etPn.getText().toString();
                PropertyManager.getInstance().setPn(pn);

                // 서버와 pn 통신
                NetworkManager.getInstance().confirmPn(getActivity(), pn, new NetworkManager.OnNetworkResultListener<Res>() {
                    @Override
                    public void onResult(Res res) {
                        if (res.error == null) {
                            String token = res.Authorization;
                            Toast.makeText(getActivity(), "already registered. token : " + token, Toast.LENGTH_SHORT).show();
                            // TODO: 백업한 데이터 받고 Main으로
//                            startActivity(new Intent(getActivity(), MainActivity.class));
                        }
                    }

                    @Override
                    public void onFail(Res res) {
                        if(res != null)
                        if (res.error.equals("no user")) {
                            ((TutorialActivity)getActivity()).onNextClicked();
                            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(
                                    Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(etPn.getWindowToken(), 0);
                        } else if (res.error.equals("invalid")) {
                            Toast.makeText(getActivity(), "invalid pn", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
        return rootView;
    }
}
