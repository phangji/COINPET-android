package com.quadcoder.coinpet.network;

import android.content.Context;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.quadcoder.coinpet.MyApplication;
import com.quadcoder.coinpet.network.response.Cost;
import com.quadcoder.coinpet.network.response.Res;

import org.apache.http.Header;

/**
 * Created by Phangji on 4/5/15.
 */
public class NetworkModel {
    private static NetworkModel instance;

    public static NetworkModel getInstance() {
        if(instance == null)
            instance = new NetworkModel();
        return instance;
    }

    private NetworkModel() {
        client = new AsyncHttpClient();
        client.setCookieStore(new PersistentCookieStore(MyApplication
                .getContext()));
        client.setTimeout(30000);
    }

    AsyncHttpClient client;

    public static final String SERVER_INTERNAL_URL = "http://172.16.100.181:3300";
    public static final String SERVER_EXTERNAL_URL = "http://61.43.139.155:3300";
    public static final String SERVER_URL = SERVER_INTERNAL_URL;

    public interface OnNetworkResultListener<T> {
        public void onResult(T res);

        public void onFail(int code);
    }

    public void cancelRequests(Context context) {
        client.cancelRequests(context, false);
    }

    public void sendCoin(Context context, int money,
                     final OnNetworkResultListener<Cost> listener) {
        String url = SERVER_URL + "/root/coin";  //api
        RequestParams params = new RequestParams();
        params.put("cost", money);
        client.post(context, url, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                Gson gson = new Gson();
                Cost result = gson.fromJson(response, Cost.class);
                listener.onResult(result);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                listener.onFail(statusCode);

            }
        });
    }

    public void signup(Context context, String name, int gender, int age,
                         final OnNetworkResultListener<Res> listener) {
        String url = SERVER_URL + "/user/kids";  //api
        RequestParams params = new RequestParams();
        params.put("name", name);
        params.put("gender", gender);   //api에서 valueof로 변환 해줌.
        params.put("age", age);

        client.post(context, url, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                Gson gson = new Gson();
                Res result = gson.fromJson(response, Res.class);
                listener.onResult(result);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                listener.onFail(statusCode);

            }
        });
    }

}
