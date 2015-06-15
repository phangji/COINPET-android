package com.quadcoder.coinpet.page.quiz;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.quadcoder.coinpet.R;

import java.util.ArrayList;
import java.util.List;

public class QuizActivity extends ActionBarActivity {

    private int pageIndex = 0;
    public static final String INTENT_MONEY = "moneyList";

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        moneyList = new ArrayList<>();

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
        } else if( id == android.R.id.home) {
            sendEventToMain();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    ArrayList<Integer> moneyList;

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
//        if(pageIndex == 0)
//            finish();
//        else if(pageIndex == 1) {
//            FragmentManager fm = getSupportFragmentManager();
//            List<Fragment> list = fm.getFragments();
//            Log.d("phangji fragment count", " " + list.size());
//            QuizFragment fragment = (QuizFragment)list.get(1);
//            fragment.sendEventToMain();
//        }
        sendEventToMain();

    }

    void sendEventToMain() {
        // 이벤트들을 intent에 담아서, 이 액티비티가 task에서 빠지고, main이 시작될 때 main에게 전해준다.
        if(moneyList.size() > 0) {
            Intent result = new Intent();
            result.putExtra(INTENT_MONEY, moneyList);
            setResult(Activity.RESULT_OK, result);
            finish();
        } else {
            finish();
        }
    }
}
