package com.quadcoder.coinpet.model;

/**
 * Created by Phangji on 5/20/15.
 */
public class Friend {

    public static final int MAMA = 1;
    public static final int SLEEPING = 2;
    public static final int CHECKCHECK = 3;
    public static final int SMART = 4;
    public static final int MORNING = 5;

    public int pk;  //리스트에 보여지는 순서이자 pk
    public String name;
    public String description;
    public String condition;
    public boolean isSaved;
    public int resId;

    public Friend(int pk, String name, String description, String condition, boolean isSaved, int resId) {
        this.pk = pk;
        this.name = name;
        this.description = description;
        this.condition = condition;
        this.isSaved = isSaved;
        this.resId = resId;
    }

    public Friend() {
    }
}
