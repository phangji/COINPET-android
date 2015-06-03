package com.quadcoder.coinpet.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBOpenHelper extends SQLiteOpenHelper {

	private final static String DB_NAME = "coinpet.db";
	private final static int DB_VERSION = 1;
	
	public MyDBOpenHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		String sql = "CREATE TABLE " + DBConstants.SystemQuestTable.TABLE_NAME + "("
				+ DBConstants.SystemQuestTable.PK + " integer, "
				+ DBConstants.SystemQuestTable.CONTENT + " text, "
				+ DBConstants.SystemQuestTable.POINT + " integer, "
				+ DBConstants.SystemQuestTable.ORDER + " integer, "
				+ DBConstants.SystemQuestTable.STATE + " integer); ";
		db.execSQL(sql);

		sql = "CREATE TABLE " + DBConstants.ParentQuestTable.TABLE_NAME + "("
				+ DBConstants.ParentQuestTable.PK + " integer, "
				+ DBConstants.ParentQuestTable.CONTENT + " text, "
				+ DBConstants.ParentQuestTable.POINT + " integer, "
				+ DBConstants.ParentQuestTable.START_TIME + " text, "
				+ DBConstants.ParentQuestTable.COMMENT + " text, "
				+ DBConstants.ParentQuestTable.STATE + " integer); ";
		db.execSQL(sql);

		sql = "CREATE TABLE " + DBConstants.QuizTable.TABLE_NAME + "("
				+ DBConstants.QuizTable.PK + " integer, "
				+ DBConstants.QuizTable.CONTENT + " text, "
				+ DBConstants.QuizTable.POINT + " integer, "
				+ DBConstants.QuizTable.DIFF + " integer, "
				+ DBConstants.QuizTable.HINT + " text, "
				+ DBConstants.QuizTable.TIME + " integer, "
				+ DBConstants.QuizTable.SOLUTION + " integer, "
                + DBConstants.QuizTable.EXPLANATION + " text, "
				+ DBConstants.QuizTable.STATE + " integer); ";
		db.execSQL(sql);

		sql = "CREATE TABLE " + DBConstants.FriendsTable.TABLE_NAME + "("
				+ DBConstants.FriendsTable.PK + " integer, "
				+ DBConstants.FriendsTable.NAME + " text, "
				+ DBConstants.FriendsTable.DESCRIPTION + " integer, "
				+ DBConstants.FriendsTable.CONDITION + " text, "
				+ DBConstants.FriendsTable.RESOURCE_ID + " integer, "
				+ DBConstants.FriendsTable.IS_SAVED + " integer); ";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}
}
