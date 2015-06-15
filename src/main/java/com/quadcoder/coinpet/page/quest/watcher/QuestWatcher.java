package com.quadcoder.coinpet.page.quest.watcher;

import com.quadcoder.coinpet.database.DBManager;
import com.quadcoder.coinpet.model.Quest;
import com.quadcoder.coinpet.model.SystemQuest;
import com.quadcoder.coinpet.network.response.Saving;
import com.quadcoder.coinpet.page.common.PropertyManager;

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
    boolean needUpdate = false;

    private static class QuestWatcherHolder {
        private static final QuestWatcher instance = new QuestWatcher();
    }

    public static QuestWatcher getInstance() {
        return QuestWatcherHolder.instance;
    }

    private QuestWatcher() {
        mActiveList = DBManager.getInstance().getActiveSystemQuestList();
    }

    public void listenAction(String type, String method) { //saving이면 method가 null이어되 됌.

        needUpdate = true;

        if(type.equals(Parsing.Type.SAVING)) {   //method = anytime, morning, evening

            for(SystemQuest quest : mActiveList) {
                if(quest.con_type.equals(type)) {   //type이 saving인데,
                    if(quest.con_method.equals(Parsing.Method.ANYTIME)) {
                        updateCount(quest);
                    } else if(quest.con_method.equals(Parsing.Method.MORNING)) {
                        //아침 시간대 판별
                        if( getTimeMarkerString().equals("오전") || getTimeMarkerString().equals("AM") ) {
                            updateCount(quest);
                            PropertyManager.getInstance().plusMorningSaving();
                        }
                    } else if(quest.con_method.equals(Parsing.Method.EVENING)) {
                        //저녁 시간대 판별
                        if( getTimeMarkerString().equals("오후") || getTimeMarkerString().equals("PM") ) {
                            updateCount(quest);
                        }
                    }
                }
            }
        } else if(type.equals(Parsing.Type.STD_QUEST)) {

            for(SystemQuest quest : mActiveList) {
                if(quest.con_type.equals(type) && quest.con_method.equals(method)) {
                    updateCount(quest);
                }
            }

        } else if(type.equals(Parsing.Type.PARENT_QUEST)) {

            for(SystemQuest quest : mActiveList) {
                if(quest.con_type.equals(type) && quest.con_method.equals(method)) {
                    updateCount(quest);
                    PropertyManager.getInstance().plusParentQuest();
                }
            }
        }
    }

    public void saveChanges() {     // 호출할 필요가 없을 듯.
        if(needUpdate)
            for(SystemQuest record : mActiveList) {
                DBManager.getInstance().updateActiveSystemQuestCountAndState(record);
            }
        needUpdate = false;
    }

    public void removeActiveQuest(SystemQuest quest) {
        DBManager.getInstance().deleteActiveSystemQuest(quest);
        mActiveList.clear();
        mActiveList = DBManager.getInstance().getActiveSystemQuestList();
    }

    public void refreshActiveList() {
        saveChanges();
        mActiveList = DBManager.getInstance().getActiveSystemQuestList();
        needUpdate = true;
    }

    private String getTimeMarkerString() {
        SimpleDateFormat now = new SimpleDateFormat("a", Locale.KOREA);
        String text= now.format(new Date()).toString();
        return text;
    }

    private void updateCount(SystemQuest quest) {
        if(quest.con_count > 0)
            quest.con_count --;

        if(quest.con_count <= 0 && quest.state == Quest.DOING) {
            // count가 0이 되었을 때는 state를 업데이트 --> 각각의 퀘스트가 0이 되는 순간에만 update
            quest.state = Quest.FINISHED;
            DBManager.getInstance().updateActiveSystemQuestState(quest);
        }
    }
}
