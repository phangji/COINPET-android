package com.quadcoder.coinpet.model;

/**
 * Created by Phangji on 5/20/15.
 */
public interface QuestState {
    int CREATE = 1;
    int DOING = 2;
    int WAITING = 3;
    int RETRY = 4;
    int FINISH = 5;
    int DELETE = 6;
}
