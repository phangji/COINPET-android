package com.quadcoder.coinpet.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.quadcoder.coinpet.page.common.MyApplication;
import com.quadcoder.coinpet.database.DBConstants.SystemQuestTable;
import com.quadcoder.coinpet.database.DBConstants.ActiveSystemQuestTable;
import com.quadcoder.coinpet.database.DBConstants.ParentQuestTable;
import com.quadcoder.coinpet.database.DBConstants.QuizTable;
import com.quadcoder.coinpet.database.DBConstants.FriendsTable;
import com.quadcoder.coinpet.model.Friend;
import com.quadcoder.coinpet.model.Quest;
import com.quadcoder.coinpet.model.ParentQuest;
import com.quadcoder.coinpet.model.Quiz;
import com.quadcoder.coinpet.model.SystemQuest;

public class DBManager {
    private static class DBManagerHolder {
        private static final DBManager instance = new DBManager();
    }

	public static DBManager getInstance() {
		return DBManagerHolder.instance;
	}

	MyDBOpenHelper mHelper;

	private DBManager() {
		mHelper = new MyDBOpenHelper(MyApplication.getContext());
	}

    /**
     * SELECT
     */


    public SystemQuest getSystemQuestRandom() {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        String[] columns = {SystemQuestTable.PK, SystemQuestTable.CONTENT, SystemQuestTable.POINT, SystemQuestTable.STATE, SystemQuestTable.CON_TYPE, SystemQuestTable.CON_METHOD, SystemQuestTable.CON_COUNT};
        String orderBy = "random()";
        Cursor c = db.query(SystemQuestTable.TABLE_NAME, columns, null, null, null, null, orderBy);

        SystemQuest record = new SystemQuest();
        while (c.moveToNext()) {
            record.pk_std_que = c.getInt(c.getColumnIndex(SystemQuestTable.PK));
            record.content = c.getString(c.getColumnIndex(SystemQuestTable.CONTENT));
            record.point = c.getInt(c.getColumnIndex(SystemQuestTable.POINT));
            record.state = c.getInt(c.getColumnIndex(SystemQuestTable.STATE));
            record.con_type = c.getString(c.getColumnIndex(SystemQuestTable.CON_TYPE));
            record.con_method = c.getString(c.getColumnIndex(SystemQuestTable.CON_METHOD));
            record.con_count = c.getInt(c.getColumnIndex(SystemQuestTable.CON_COUNT));
        }
        c.close();

        return record;
    }


    /**
     * SAVE
     */

    public SystemQuest createNewActiveSystemQuest() {

        // 현재 액티브 퀘스트에 없는 것을 시스템 퀘스트로부터 가져온다.
        SystemQuest activeQuest = null;

        do {
            activeQuest = getSystemQuestRandom();
            if( insertActiveSystemQuest(activeQuest) ) {
                activeQuest = null;
            }
        } while (activeQuest == null);

        return activeQuest;
    }

    public ArrayList<SystemQuest> getActiveSystemQuestList() {
        ArrayList<SystemQuest> list = new ArrayList<SystemQuest>();
        SQLiteDatabase db = mHelper.getReadableDatabase();
        String[] columns = {SystemQuestTable.PK, SystemQuestTable.CONTENT, SystemQuestTable.POINT, SystemQuestTable.STATE, SystemQuestTable.CON_TYPE, SystemQuestTable.CON_METHOD, SystemQuestTable.CON_COUNT};
        String orderBy = SystemQuestTable.PK + " ASC";
        String selection = SystemQuestTable.STATE + " != ? ";
        String[] selectionArgs = { "" + Quest.DELETED };
        Cursor c = db.query(ActiveSystemQuestTable.TABLE_NAME, columns, selection, selectionArgs, null, null, orderBy);

        if(c.getCount() < 3)
        while (c.moveToNext()) {
            SystemQuest record = new SystemQuest();
            record.pk_std_que = c.getInt(c.getColumnIndex(SystemQuestTable.PK));
            record.content = c.getString(c.getColumnIndex(SystemQuestTable.CONTENT));
            record.point = c.getInt(c.getColumnIndex(SystemQuestTable.POINT));
            record.state = c.getInt(c.getColumnIndex(SystemQuestTable.STATE));
            record.con_type = c.getString(c.getColumnIndex(SystemQuestTable.CON_TYPE));
            record.con_method = c.getString(c.getColumnIndex(SystemQuestTable.CON_METHOD));
            record.con_count = c.getInt(c.getColumnIndex(SystemQuestTable.CON_COUNT));

            list.add(record);
        }
        c.close();

        return list;
    }

    public ArrayList<ParentQuest> getParentQuestList() {
        ArrayList<ParentQuest> list = new ArrayList<ParentQuest>();
        SQLiteDatabase db = mHelper.getReadableDatabase();
        String[] columns = {ParentQuestTable.PK, ParentQuestTable.CONTENT, ParentQuestTable.POINT, ParentQuestTable.STATE, ParentQuestTable.START_TIME};
        String selection = "DATE(" + ParentQuestTable.START_TIME + ", 'utc') < DATE('now', 'utc') AND " + ParentQuestTable.STATE + " != ? ";
        String[] selectionArgs = { "" + Quest.DELETED};
        Cursor c = db.query(ParentQuestTable.TABLE_NAME, columns, selection, selectionArgs, null, null, null);

        while (c.moveToNext()) {
            ParentQuest record = new ParentQuest();
            record.pk_parents_quest = c.getInt(c.getColumnIndex(ParentQuestTable.PK));
            record.content = c.getString(c.getColumnIndex(ParentQuestTable.CONTENT));
            record.point = c.getInt(c.getColumnIndex(ParentQuestTable.POINT));
            record.state = c.getInt(c.getColumnIndex(ParentQuestTable.STATE));
            record.startTime = c.getString(c.getColumnIndex(ParentQuestTable.START_TIME));
            list.add(record);
        }
        c.close();

        return list;
    }

    public Quiz getQuizRandom() {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        String[] columns = {QuizTable.PK, QuizTable.CONTENT, QuizTable.POINT, QuizTable.DIFF, QuizTable.HINT, QuizTable.TIME, QuizTable.SOLUTION, QuizTable.ANSWER, QuizTable.STATE};
        String selection = QuizTable.STATE + " = ?  OR " + QuizTable.STATE + " = ? limit 1";
        String[] selectionArgs = { "" + Quiz.STATE_YET,  "" + Quiz.STATE_WRONG};
        String orderBy = "random()";
        Cursor c = db.query(QuizTable.TABLE_NAME, columns, selection, selectionArgs, null, null, orderBy);

        Quiz record = new Quiz();
        while (c.moveToNext()) {
            record.pk_std_quiz = c.getInt(c.getColumnIndex(QuizTable.PK));
            record.content = c.getString(c.getColumnIndex(QuizTable.CONTENT));
            record.point = c.getInt(c.getColumnIndex(QuizTable.POINT));
            record.state = c.getInt(c.getColumnIndex(QuizTable.STATE));
            record.level = c.getInt(c.getColumnIndex(QuizTable.DIFF));
            record.hint = c.getString(c.getColumnIndex(QuizTable.HINT));
            record.time = c.getInt(c.getColumnIndex(QuizTable.TIME));
            record.solution = c.getString(c.getColumnIndex(QuizTable.SOLUTION));
            record.answer = c.getInt(c.getColumnIndex(QuizTable.ANSWER));
        }
        c.close();

        return record;
    }

    public ArrayList<Quiz> getQuizList() {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        String[] columns = {QuizTable.PK, QuizTable.CONTENT, QuizTable.POINT, QuizTable.DIFF, QuizTable.HINT, QuizTable.TIME, QuizTable.SOLUTION, QuizTable.ANSWER, QuizTable.STATE};
        String orderBy = "random()";
        Cursor c = db.query(QuizTable.TABLE_NAME, columns, null, null, null, null, orderBy);

        ArrayList<Quiz> list = new ArrayList<>();

        while (c.moveToNext()) {
            Quiz record = new Quiz();
            record.pk_std_quiz = c.getInt(c.getColumnIndex(QuizTable.PK));
            record.content = c.getString(c.getColumnIndex(QuizTable.CONTENT));
            record.point = c.getInt(c.getColumnIndex(QuizTable.POINT));
            record.state = c.getInt(c.getColumnIndex(QuizTable.STATE));
            record.level = c.getInt(c.getColumnIndex(QuizTable.DIFF));
            record.hint = c.getString(c.getColumnIndex(QuizTable.HINT));
            record.time = c.getInt(c.getColumnIndex(QuizTable.TIME));
            record.solution = c.getString(c.getColumnIndex(QuizTable.SOLUTION));
            record.answer = c.getInt(c.getColumnIndex(QuizTable.ANSWER));
            list.add(record);
        }
        c.close();

        return list;
    }

    public ArrayList<Friend> getFriendList() {
        ArrayList<Friend> list = new ArrayList<Friend>();
        SQLiteDatabase db = mHelper.getReadableDatabase();
        String[] columns = {FriendsTable.PK, FriendsTable.NAME, FriendsTable.DESCRIPTION, FriendsTable.CONDITION, FriendsTable.IS_SAVED, FriendsTable.RESOURCE_ID};
        String orderBy = FriendsTable.PK + " ASC";
        Cursor c = db.query(FriendsTable.TABLE_NAME, columns, null, null, null, null, orderBy);

        while (c.moveToNext()) {
            Friend record = new Friend();
            record.pk = c.getInt(c.getColumnIndex(FriendsTable.PK));
            record.name = c.getString(c.getColumnIndex(FriendsTable.NAME));
            record.description = c.getString(c.getColumnIndex(FriendsTable.DESCRIPTION));
            record.condition = c.getString(c.getColumnIndex(FriendsTable.CONDITION));
            record.isSaved = c.getInt(c.getColumnIndex(FriendsTable.IS_SAVED)) == 1 ? true : false;
            record.resId = c.getInt(c.getColumnIndex(FriendsTable.RESOURCE_ID));
            list.add(record);
        }
        c.close();

        return list;
    }

    /**
     * INSERT
     */

    public void insertSystemQuest(SystemQuest record) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SystemQuestTable.PK, record.pk_std_que);
        values.put(SystemQuestTable.CONTENT, record.content);
        values.put(SystemQuestTable.POINT, record.point);
        values.put(SystemQuestTable.STATE, record.state);
        values.put(SystemQuestTable.CON_TYPE, record.con_type);
        values.put(SystemQuestTable.CON_METHOD, record.con_method);
        values.put(SystemQuestTable.CON_COUNT, record.con_count);
        db.insert(SystemQuestTable.TABLE_NAME, null, values);
        db.close();
    }

    public boolean insertActiveSystemQuest(SystemQuest record) {
        boolean isInserted = true;

        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SystemQuestTable.PK, record.pk_std_que);
        values.put(SystemQuestTable.CONTENT, record.content);
        values.put(SystemQuestTable.POINT, record.point);
        values.put(SystemQuestTable.STATE, record.state);
        values.put(SystemQuestTable.CON_TYPE, record.con_type);
        values.put(SystemQuestTable.CON_METHOD, record.con_method);
        values.put(SystemQuestTable.CON_COUNT, record.con_count);

        long val = db.insert(ActiveSystemQuestTable.TABLE_NAME, null, values);
        if(val == -1)
            isInserted = false;
        db.close();
        return isInserted;
    }

    public void insertParentQuest(ParentQuest record) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ParentQuestTable.PK, record.pk_parents_quest);
        values.put(ParentQuestTable.CONTENT, record.content);
        values.put(ParentQuestTable.POINT, record.point);
        values.put(ParentQuestTable.STATE, record.state);
        values.put(ParentQuestTable.START_TIME, record.startTime);
        db.insert(ParentQuestTable.TABLE_NAME, null, values);
        db.close();
    }

    public void insertQuiz(Quiz record) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(QuizTable.PK, record.pk_std_quiz);
        values.put(QuizTable.CONTENT, record.content);
        values.put(QuizTable.POINT, record.point);
        values.put(QuizTable.STATE, record.state);
        values.put(QuizTable.DIFF, record.level);
        values.put(QuizTable.HINT, record.hint);
        values.put(QuizTable.TIME, record.time);
        values.put(QuizTable.SOLUTION, record.solution);
        values.put(QuizTable.ANSWER, record.answer);
        db.insert(QuizTable.TABLE_NAME, null, values);
        db.close();
    }

    public void insertFreind(Friend record) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FriendsTable.PK, record.pk);
        values.put(FriendsTable.NAME, record.name);
        values.put(FriendsTable.DESCRIPTION, record.description);
        values.put(FriendsTable.CONDITION, record.condition);
        values.put(FriendsTable.IS_SAVED, record.isSaved);
        values.put(FriendsTable.RESOURCE_ID, record.resId);
        db.insert(FriendsTable.TABLE_NAME, null, values);
        db.close();
    }


    /**
     * UPDATE
     */

    public void updateSystemQuest(SystemQuest record){
        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SystemQuestTable.STATE, record.state);
        String whereClause = SystemQuestTable.PK + " = ? ";
        String[] whereArgs = { "" + record.pk_std_que};
        db.update(SystemQuestTable.TABLE_NAME, values, whereClause, whereArgs);
        db.close();
    }   //TODO:

    public void updateActiveSystemQuestState(SystemQuest record){
        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SystemQuestTable.STATE, record.state);
        String whereClause = SystemQuestTable.PK + " = ? ";
        String[] whereArgs = { "" + record.pk_std_que};
        db.update(ActiveSystemQuestTable.TABLE_NAME, values, whereClause, whereArgs);
        db.close();
    }

    public void updateActiveSystemQuestCount(SystemQuest record){
        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SystemQuestTable.CON_COUNT, record.con_count);
        String whereClause = SystemQuestTable.PK + " = ? ";
        String[] whereArgs = { "" + record.pk_std_que};
        db.update(ActiveSystemQuestTable.TABLE_NAME, values, whereClause, whereArgs);
        db.close();
    }

    public void updateParentQuestState(ParentQuest record){
        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ParentQuestTable.STATE, record.state);
        String whereClause = ParentQuestTable.PK + " = ? ";
        String[] whereArgs = { "" + record.pk_parents_quest};
        db.update(ParentQuestTable.TABLE_NAME, values, whereClause, whereArgs);
        db.close();
    }

    public void updateParentQuest(ParentQuest record){
        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ParentQuestTable.COMMENT, record.comment);
        values.put(ParentQuestTable.POINT, record.point);
        values.put(ParentQuestTable.CONTENT, record.content);
        values.put(ParentQuestTable.START_TIME, record.startTime);
        values.put(ParentQuestTable.STATE, record.state);
        String whereClause = ParentQuestTable.PK + " = ? ";
        String[] whereArgs = { "" + record.pk_parents_quest};
        db.update(ParentQuestTable.TABLE_NAME, values, whereClause, whereArgs);
        db.close();
    }

    public void updateQuiz(Quiz record){
        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(QuizTable.STATE, record.state);
        String whereClause = QuizTable.PK + " = ? ";
        String[] whereArgs = { "" + record.pk_std_quiz};
        db.update(QuizTable.TABLE_NAME, values, whereClause, whereArgs);
        db.close();
    }

    public void updateFriend(Friend record){
        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FriendsTable.IS_SAVED, record.isSaved);
        String whereClause = FriendsTable.PK + " = ? ";
        String[] whereArgs = { "" + record.pk };
        db.update(FriendsTable.TABLE_NAME, values, whereClause, whereArgs);
        db.close();
    }

    /**
     * DELETE
     */

    public void deleteSystemQuest(SystemQuest record){
        SQLiteDatabase db = mHelper.getWritableDatabase();
        String whereClause = SystemQuestTable.PK + " = ? ";
        String[] whereArgs = { "" + record.pk_std_que};
        db.delete(SystemQuestTable.TABLE_NAME, whereClause, whereArgs);
        db.close();
    }

    public void deleteActiveSystemQuest(SystemQuest record){
        SQLiteDatabase db = mHelper.getWritableDatabase();
        String whereClause = SystemQuestTable.PK + " = ? ";
        String[] whereArgs = { "" + record.pk_std_que};
        db.delete(ActiveSystemQuestTable.TABLE_NAME, whereClause, whereArgs);
        db.close();
    }

    public void deleteParentQuest(ParentQuest record){
        SQLiteDatabase db = mHelper.getWritableDatabase();
        String whereClause = ParentQuestTable.PK + " = ? ";
        String[] whereArgs = { "" + record.pk_parents_quest};
        db.delete(ParentQuestTable.TABLE_NAME, whereClause, whereArgs);
        db.close();
    }

}
