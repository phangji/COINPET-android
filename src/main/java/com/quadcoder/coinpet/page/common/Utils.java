package com.quadcoder.coinpet.page.common;

import com.quadcoder.coinpet.R;

/**
 * Created by Phangji on 6/1/15.
 */
public class Utils {
    private static Utils ourInstance = new Utils();

    public static Utils getInstance() {
        return ourInstance;
    }

    private Utils() {
    }

    final int[] qCoinResource = { R.drawable.q_coin_0, R.drawable.q_coin_1, R.drawable.q_coin_2, R.drawable.q_coin_3 };
    final int[] diffResource = { R.string.diff_easy, R.string.diff_normal, R.string.diff_difficult };

    public int getQCoinResource(int count) {
        if(count < 0 || count > 3)
            throw new ArrayIndexOutOfBoundsException("qcoin count bound out");

        return qCoinResource[count];
    }

    public int getDiffResource(int diff) {
        if(diff < 1 || diff > 2)
            throw new ArrayIndexOutOfBoundsException("diff bound out");

        return diffResource[diff - 1];
    }
}
