package com.quadcoder.coinpet.network.response;

import com.quadcoder.coinpet.model.ParentQuest;
import com.quadcoder.coinpet.model.Quiz;
import com.quadcoder.coinpet.model.SystemQuest;

import java.util.ArrayList;

/**
 * Created by Phangji on 6/4/15.
 */
public class UpdatedData {
    public boolean needUpdate;
    public ArrayList<Quiz> systemQuiz;
    public ArrayList<SystemQuest> systemQuest;
    public ArrayList<ParentQuest> parentsQuest;
}
