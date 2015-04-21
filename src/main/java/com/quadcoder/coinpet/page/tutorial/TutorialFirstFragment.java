package com.quadcoder.coinpet.page.tutorial;


import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.quadcoder.coinpet.PropertyManager;
import com.quadcoder.coinpet.R;
import com.quadcoder.coinpet.logger.Log;
import com.quadcoder.coinpet.network.NetworkModel;
import com.quadcoder.coinpet.network.response.Res;
import com.quadcoder.coinpet.page.common.Constants;

/**
 * A simple {@link Fragment} subclass.
 */
public class TutorialFirstFragment extends Fragment {


    public TutorialFirstFragment() {
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
                NetworkModel.getInstance().confirmPn(getActivity(), pn, new NetworkModel.OnNetworkResultListener<Res>() {
                    @Override
                    public void onResult(Res res) {
                        if (res.error == null) {
                            String token = res.Authorization;
                            Toast.makeText(getActivity(), "already registered. token : " + token, Toast.LENGTH_SHORT).show();
                            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(
                                    Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(etPn.getWindowToken(), 0);
                        }
                    }

                    @Override
                    public void onFail(Res res) {
                        if(res != null)
                        if (res.error.equals("no user")) {
                            ((TutorialActivity)getActivity()).onNextClicked();
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
