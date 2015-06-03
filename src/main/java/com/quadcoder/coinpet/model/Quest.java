package com.quadcoder.coinpet.model;

/**
 * Created by Phangji on 5/20/15.
 */
public interface Quest {
    int CREATED = 1;    // 처음 생겨난 상태
    int DOING = 2;      // 퀘스트를 수행 중
    int WAITING = 3;    // [부모]검사 기다리는 중
    int RETRYING = 4;      // [부모]검사 다시 해야
    int FINISHED = 5;
    int DELETED = 6;
}
