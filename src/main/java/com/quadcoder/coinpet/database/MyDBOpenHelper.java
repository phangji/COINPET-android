package com.quadcoder.coinpet.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.quadcoder.coinpet.R;
import com.quadcoder.coinpet.model.Friend;

public class MyDBOpenHelper extends SQLiteOpenHelper {

	private final static String DB_NAME = "coinpet.db";
	private final static int DB_VERSION = 1;
	
	public MyDBOpenHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		String sql = "CREATE TABLE " + DBConstants.SystemQuestTable.TABLE_NAME + "("
				+ DBConstants.SystemQuestTable.PK
				+ " integer PRIMARY KEY , "
				+ DBConstants.SystemQuestTable.CONTENT + " text, "
				+ DBConstants.SystemQuestTable.POINT + " integer, "
				+ DBConstants.SystemQuestTable.CON_TYPE + " text, "
				+ DBConstants.SystemQuestTable.CON_METHOD + " text, "
				+ DBConstants.SystemQuestTable.CON_COUNT + " integer, "
				+ DBConstants.SystemQuestTable.STATE + " integer); ";
		db.execSQL(sql);

        sql = "CREATE TABLE " + DBConstants.ActiveSystemQuestTable.TABLE_NAME + "("
                + DBConstants.SystemQuestTable.PK
                + " integer PRIMARY KEY , "
                + DBConstants.SystemQuestTable.CONTENT + " text, "
                + DBConstants.SystemQuestTable.POINT + " integer, "
                + DBConstants.SystemQuestTable.CON_TYPE + " text, "
                + DBConstants.SystemQuestTable.CON_METHOD + " text, "
                + DBConstants.SystemQuestTable.CON_COUNT + " integer, "
                + DBConstants.SystemQuestTable.STATE + " integer); ";
        db.execSQL(sql);

		sql = "CREATE TABLE " + DBConstants.ParentQuestTable.TABLE_NAME + "("
				+ DBConstants.ParentQuestTable.PK
				+ " integer PRIMARY KEY , "
				+ DBConstants.ParentQuestTable.CONTENT + " text, "
				+ DBConstants.ParentQuestTable.POINT + " integer, "
				+ DBConstants.ParentQuestTable.START_TIME + " text, "
				+ DBConstants.ParentQuestTable.COMMENT + " text, "
				+ DBConstants.ParentQuestTable.STATE + " integer); ";
		db.execSQL(sql);

		sql = "CREATE TABLE " + DBConstants.QuizTable.TABLE_NAME + "("
				+ DBConstants.QuizTable.PK
				+ " integer PRIMARY KEY , "
				+ DBConstants.QuizTable.CONTENT + " text, "
				+ DBConstants.QuizTable.POINT + " integer, "
				+ DBConstants.QuizTable.DIFF + " integer, "
				+ DBConstants.QuizTable.HINT + " text, "
				+ DBConstants.QuizTable.TIME + " integer, "
				+ DBConstants.QuizTable.SOLUTION + " text, "
                + DBConstants.QuizTable.ANSWER + " integer, "
				+ DBConstants.QuizTable.STATE + " integer); ";
		db.execSQL(sql);

		sql = "CREATE TABLE " + DBConstants.FriendsTable.TABLE_NAME + "("
				+ DBConstants.FriendsTable.PK
				+ " integer PRIMARY KEY , "
				+ DBConstants.FriendsTable.NAME + " text, "
				+ DBConstants.FriendsTable.DESCRIPTION + " integer, "
				+ DBConstants.FriendsTable.CONDITION + " text, "
				+ DBConstants.FriendsTable.RESOURCE_ID + " integer, "
				+ DBConstants.FriendsTable.IS_SAVED + " integer); ";
		db.execSQL(sql);

		insertIntoFreinds(db);

	}

	private void insertIntoFreinds(SQLiteDatabase db) {
		Friend[] friendList = new Friend[5];
		friendList[0] = new Friend(1, "마마", "아이들을 사랑하는\n엄마 코인펫", "부모님 퀘스트 3회", false, R.drawable.f_mm);
		friendList[1] = new Friend(2, "쿠쿠", "코코의 동생으로\n잠을 좋아하는 코인펫", "튜토리얼 클리어", false, R.drawable.f_kuku);
		friendList[2] = new Friend(3, "첵첵", "CHECK~!\n흥이 많은 코인펫", "출석 체크 20회", false, R.drawable.f_tt);
		friendList[3] = new Friend(4, "똑똑", "모아모아 마을에서\n가장 똑똑한 코인펫", "10문제 연속 정답", false, R.drawable.f_dd);
		friendList[4] = new Friend(5, "꼬꼬", "아침 일찍 일어나는\n부지런한 코인펫", "아침 시간 저금 3회", false, R.drawable.f_kk);



        try {
            db.beginTransaction();

            for(int i=0; i<5; i++) {
                Friend record = friendList[i];
                ContentValues values = new ContentValues();
                values.put(DBConstants.FriendsTable.PK, record.pk);
                values.put(DBConstants.FriendsTable.NAME, record.name);
                values.put(DBConstants.FriendsTable.DESCRIPTION, record.description);
                values.put(DBConstants.FriendsTable.CONDITION, record.condition);
                values.put(DBConstants.FriendsTable.IS_SAVED, record.isSaved ? 1 : 0 );
                values.put(DBConstants.FriendsTable.RESOURCE_ID, record.resId);
                db.insert(DBConstants.FriendsTable.TABLE_NAME, null, values);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }


	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}
}
