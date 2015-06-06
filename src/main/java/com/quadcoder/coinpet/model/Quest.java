package com.quadcoder.coinpet.model;

/**
 * Created by Phangji on 5/20/15.
 */
public interface Quest {
    int DOING = 1;      // 퀘스트를 수행 중
    int WAITING = 2;    // [부모]검사 기다리는 중
    int RETRYING = 3;      // [부모]검사 다시 해야
    int FINISHED = 4;
    int DELETED = 5;

    /**
     * System Quest
     * DOING --> FINISHED --> DELETED
     * */

    /**
     * Parent Quest
     * DOING --> WAITING --> [RETRYING] FINISHED --> DELETED
     * */
}
