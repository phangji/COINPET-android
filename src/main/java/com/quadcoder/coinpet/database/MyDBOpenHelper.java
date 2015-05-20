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

		String sql = "CREATE TABLE " + DBConstant.SystemQuestTable.TABLE_NAME + "("
				+ DBConstant.SystemQuestTable.PK + " integer, "
				+ DBConstant.SystemQuestTable.CONTENT + " text, "
				+ DBConstant.SystemQuestTable.POINT + " integer, "
				+ DBConstant.SystemQuestTable.ORDER + " integer, "
				+ DBConstant.SystemQuestTable.STATUS + " integer); ";
		db.execSQL(sql);

		sql = "CREATE TABLE " + DBConstant.ParentQuestTable.TABLE_NAME + "("
				+ DBConstant.ParentQuestTable.PK + " integer, "
				+ DBConstant.ParentQuestTable.CONTENT + " text, "
				+ DBConstant.ParentQuestTable.POINT + " integer, "
				+ DBConstant.ParentQuestTable.START_TIME + " text, "
				+ DBConstant.ParentQuestTable.STATUS + " integer); ";
		db.execSQL(sql);

		sql = "CREATE TABLE " + DBConstant.QuizTable.TABLE_NAME + "("
				+ DBConstant.QuizTable.PK + " integer, "
				+ DBConstant.QuizTable.CONTENT + " text, "
				+ DBConstant.QuizTable.POINT + " integer, "
				+ DBConstant.QuizTable.STATUS + " integer); ";
		db.execSQL(sql);

		sql = "CREATE TABLE " + DBConstant.FriendsTable.TABLE_NAME + "("
				+ DBConstant.FriendsTable.PK + " integer, "
				+ DBConstant.FriendsTable.NAME + " text, "
				+ DBConstant.FriendsTable.DESCRIPTION + " integer, "
				+ DBConstant.FriendsTable.CONDITION + " text, "
				+ DBConstant.FriendsTable.RESOURCE_ID + " integer, "
				+ DBConstant.FriendsTable.IS_SAVED + " integer); ";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}
}
