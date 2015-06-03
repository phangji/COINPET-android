package com.quadcoder.coinpet.database;

public class DBConstants {

	public static class SystemQuestTable {
		public static final String TABLE_NAME = "system_quest";
		public static final String PK = "pk";
		public static final String CONTENT = "content";
		public static final String POINT = "point";
		public static final String ORDER = "seq";
		public static final String STATE = "state";
	}

	public static class ParentQuestTable {
		public static final String TABLE_NAME = "parent_quest";
		public static final String PK = "pk";
		public static final String CONTENT = "content";
		public static final String POINT = "point";
		public static final String STATE = "state";
		public static final String COMMENT = "comment";
		public static final String START_TIME = "startTime";
	}

	public static class QuizTable {
		public static final String TABLE_NAME = "quiz";
		public static final String PK = "pk";
		public static final String CONTENT = "content";
		public static final String POINT = "point";
		public static final String STATE = "state";
		public static final String DIFF = "diff";
		public static final String HINT = "hint";
		public static final String TIME = "time";
        public static final String SOLUTION = "solution";
        public static final String EXPLANATION = "explanation";
	}

	public static class FriendsTable {
		public static final String TABLE_NAME = "friends";
		public static final String PK = "pk";
		public static final String NAME = "name";
		public static final String DESCRIPTION = "description";
		public static final String CONDITION = "condition";
		public static final String IS_SAVED = "isSaved";
		public static final String RESOURCE_ID = "resId";
	}
}
