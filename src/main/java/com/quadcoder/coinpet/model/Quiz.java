package com.quadcoder.coinpet.model;

/**
 * Created by Phangji on 5/20/15.
 */
public class Quiz {
    public static final int STATE_CORRECT = 1; // 시도 후 정답을 맞춤
    public static final int STATE_WRONG = 2;   // 시도 후 답을 틀림
    public static final int STATE_YET = 0;     //아직 시도하지 않음

    public int pk_std_quiz;
    public String content;
    public int point;
    public int state;
    public int level;
    public String hint;
    public int time;
    public String solution;
    public int answer;
}
