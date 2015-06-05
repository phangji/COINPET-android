package com.quadcoder.coinpet.network.response;

/**
 * Created by Phangji on 4/22/15.
 */
public class Goal {

    public static final int DOING = 1;
    public static final int SUCCESS = 2;
    public static final int FAIL = 3;

    public int method;
    public String content;
    public String goal_date;
    public int goal_cost;
    public int now_cost;
    public int plus;
    public int pk_goal;
    public String date;

}
