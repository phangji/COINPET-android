package com.quadcoder.coinpet.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.quadcoder.coinpet.MyApplication;
import com.quadcoder.coinpet.database.DBConstants.SystemQuestTable;
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

    public ArrayList<SystemQuest> getSystemQuestList() {
        ArrayList<SystemQuest> list = new ArrayList<SystemQuest>();
        SQLiteDatabase db = mHelper.getReadableDatabase();
        String[] columns = {SystemQuestTable.PK, SystemQuestTable.CONTENT, SystemQuestTable.POINT, SystemQuestTable.STATE};
        String orderBy = SystemQuestTable.ORDER + " ASC";
        String selection = SystemQuestTable.STATE + " != ? ";
        String[] selectionArgs = { "" + Quest.DELETE };
        Cursor c = db.query(SystemQuestTable.TABLE_NAME, columns, selection, selectionArgs, null, null, orderBy);

        while (c.moveToNext()) {
            SystemQuest record = new SystemQuest();
            record.pk = c.getInt(c.getColumnIndex(SystemQuestTable.PK));
            record.content = c.getString(c.getColumnIndex(SystemQuestTable.CONTENT));
            record.point = c.getInt(c.getColumnIndex(SystemQuestTable.POINT));
            record.state = c.getInt(c.getColumnIndex(SystemQuestTable.STATE));
            list.add(record);
        }
        c.close();

        return list;
    }

    public ArrayList<ParentQuest> getParentQuestList() {
        ArrayList<ParentQuest> list = new ArrayList<ParentQuest>();
        SQLiteDatabase db = mHelper.getReadableDatabase();
        String[] columns = {ParentQuestTable.PK, ParentQuestTable.CONTENT, ParentQuestTable.POINT, ParentQuestTable.STATE, ParentQuestTable.START_TIME};
        String selection = "DATE('" + ParentQuestTable.START_TIME + "', 'utc') < DATE('now', 'utc') AND " + ParentQuestTable.STATE + " != ? ";
        String[] selectionArgs = { "" + Quest.DELETE};
        Cursor c = db.query(ParentQuestTable.TABLE_NAME, columns, selection, selectionArgs, null, null, null);

        while (c.moveToNext()) {
            ParentQuest record = new ParentQuest();
            record.pk = c.getInt(c.getColumnIndex(ParentQuestTable.PK));
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
        String[] columns = {QuizTable.PK, QuizTable.CONTENT, QuizTable.POINT, QuizTable.DIFF, QuizTable.HINT, QuizTable.TIME, QuizTable.SOLUTION, QuizTable.EXPLANATION};
        String selection = QuizTable.STATE + " = ?  OR " + QuizTable.STATE + " = ? limit 1";
        String[] selectionArgs = { "" + Quiz.STATE_YET,  "" + Quiz.STATE_WRONG};
        String orderBy = "random()";
        Cursor c = db.query(QuizTable.TABLE_NAME, columns, selection, selectionArgs, null, null, orderBy);

        Quiz record = new Quiz();
        while (c.moveToNext()) {
            record.pk = c.getInt(c.getColumnIndex(QuizTable.PK));
            record.content = c.getString(c.getColumnIndex(QuizTable.CONTENT));
            record.point = c.getInt(c.getColumnIndex(QuizTable.POINT));
            record.state = c.getInt(c.getColumnIndex(QuizTable.STATE));
            record.diff = c.getInt(c.getColumnIndex(QuizTable.DIFF));
            record.hint = c.getString(c.getColumnIndex(QuizTable.HINT));
            record.time = c.getInt(c.getColumnIndex(QuizTable.TIME));
            record.solution = c.getInt(c.getColumnIndex(QuizTable.SOLUTION));
            record.explanation = c.getString(c.getColumnIndex(QuizTable.EXPLANATION));
        }
        c.close();

        return record;
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
        values.put(SystemQuestTable.PK, record.pk);
        values.put(SystemQuestTable.CONTENT, record.content);
        values.put(SystemQuestTable.POINT, record.point);
        values.put(SystemQuestTable.STATE, record.state);
        values.put(SystemQuestTable.ORDER, record.order);
        db.insert(SystemQuestTable.TABLE_NAME, null, values);
        db.close();
    }

    public void insertParentQuest(ParentQuest record) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ParentQuestTable.PK, record.pk);
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
        values.put(QuizTable.PK, record.pk);
        values.put(QuizTable.CONTENT, record.content);
        values.put(QuizTable.POINT, record.point);
        values.put(QuizTable.STATE, record.state);
        values.put(QuizTable.DIFF, record.diff);
        values.put(QuizTable.HINT, record.hint);
        values.put(QuizTable.TIME, record.time);
        values.put(QuizTable.SOLUTION, record.solution);
        values.put(QuizTable.EXPLANATION, record.explanation);
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
        String[] whereArgs = { "" + record.pk };
        db.update(SystemQuestTable.TABLE_NAME, values, whereClause, whereArgs);
        db.close();
    }

    public void updateParentQuest(ParentQuest record){
        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ParentQuestTable.STATE, record.state);
        String whereClause = ParentQuestTable.PK + " = ? ";
        String[] whereArgs = { "" + record.pk };
        db.update(ParentQuestTable.TABLE_NAME, values, whereClause, whereArgs);
        db.close();
    }

    public void updateQuiz(Quiz record){
        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(QuizTable.STATE, record.state);
        String whereClause = QuizTable.PK + " = ? ";
        String[] whereArgs = { "" + record.pk };
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
        String[] whereArgs = { "" + record.pk };
        db.delete(SystemQuestTable.TABLE_NAME, whereClause, whereArgs);
        db.close();
    }

    public void deleteParentQuest(ParentQuest record){
        SQLiteDatabase db = mHelper.getWritableDatabase();
        String whereClause = ParentQuestTable.PK + " = ? ";
        String[] whereArgs = { "" + record.pk };
        db.delete(ParentQuestTable.TABLE_NAME, whereClause, whereArgs);
        db.close();
    }
}
