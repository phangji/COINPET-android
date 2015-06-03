package com.quadcoder.coinpet.page.quest;

import android.os.Handler;

/**
 * Created by Phangji on 6/3/15.
 */
public class QuestWatcher {
    private static QuestWatcher ourInstance = new QuestWatcher();

    public static QuestWatcher getInstance() {
        return ourInstance;
    }

    private QuestWatcher() {
    }

    Handler mHandler = new Handler();

    void pointUp(int point) {

    }

    void accessCountUp(int count) {

    }

}
