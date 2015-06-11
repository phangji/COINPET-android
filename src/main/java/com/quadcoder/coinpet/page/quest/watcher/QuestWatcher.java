package com.quadcoder.coinpet.page.quest.watcher;

import com.quadcoder.coinpet.database.DBManager;
import com.quadcoder.coinpet.model.SystemQuest;
import com.quadcoder.coinpet.network.response.Saving;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Phangji on 6/3/15.
 */
public class QuestWatcher {

    ArrayList<SystemQuest> mActiveList;

    private static class QuestWatcherHolder {
        private static final QuestWatcher instance = new QuestWatcher();
    }

    public static QuestWatcher getInstance() {
        return QuestWatcherHolder.instance;
    }

    private QuestWatcher() {
        mActiveList = DBManager.getInstance().getActiveSystemQuestList();
    }

    void listenAction(String type, String method) { //saving이면 method가 null이어되 됌.

        if(type == Parsing.Type.SAVING) {   //method = anytime, morning, evening

            for(SystemQuest quest : mActiveList) {
                if(quest.con_type.equals(type)) {   //type이 saving인데,
                    if(quest.con_method.equals(Parsing.Method.ANYTIME)) {
                        quest.con_count --;
                    } else if(quest.con_method.equals(Parsing.Method.MORNING)) {
                        //아침 시간대 판별
                        if( getTimeMarkerString().equals("AM") ) {
                            quest.con_count --;
                        }
                    } else if(quest.con_method.equals(Parsing.Method.EVENING)) {
                        //저녁 시간대 판별
                        if( getTimeMarkerString().equals("PM") ) {
                            quest.con_count --;
                        }
                    }
                }
            }
            return;
        }

        for(SystemQuest quest : mActiveList) {  //Active 퀘스트를 하나씩 체크
            if(quest.con_type.equals(type) && quest.con_method.equals(method)) {
                quest.con_count --;
            }
        }
    }

    public void saveChanges() {
        for(SystemQuest record : mActiveList) {
            DBManager.getInstance().updateActiveSystemQuestCount(record);
        }
    }

    private String getTimeMarkerString() {
        SimpleDateFormat now = new SimpleDateFormat("a", Locale.KOREA);
        String text= now.format(new Date()).toString();
        return text;
    }
}