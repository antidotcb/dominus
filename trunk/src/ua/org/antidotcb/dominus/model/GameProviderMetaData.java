package ua.org.antidotcb.dominus.model;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class GameProviderMetaData {

	private GameProviderMetaData() {}

	public static final String	AUTHORITY			= "ua.org.antidotcb.dominus.model.GameProvider";
	public static final String	DATABASE_NAME		= "game.db";
	public static final int		DATABASE_VERSION	= 1;

	public static final class GameTableMetaData implements BaseColumns {

		private GameTableMetaData() {}

		public static final String	TABLE_NAME			= "games";
		public static final Uri		CONTENT_URI			= Uri.parse("content://" + AUTHORITY);

		public static final String	CONTENT_TYPE		= ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.antidotcb.dominus.game";

		public static final String	CONTENT_ITEM_TYPE	= ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.antidotcb.dominus.game";

		// Columns definition begin
		public static final String	GAME_NAME			= "name";
		public static final String	GAME_START_DATE		= "created";
		public static final String	GAME_TURNS			= "turns";
		public static final String	GAME_SAVE_DATE		= "modified";
		public static final String	GAME_PLAYER			= "player";
		// @formatter:off
		private static final String COLUMN_DEFINITION = 
				  _ID				+ " INTEGER PRIMARY KEY," 
				+ GAME_NAME			+ " TEXT," 
				+ GAME_SAVE_DATE	+ " INTEGER,"
				+ GAME_START_DATE	+ " INTEGER,"
				+ GAME_TURNS		+ " INTEGER,"
				+ GAME_PLAYER		+ " INTEGER";
		// @formatter:on
		// Columns definition end

		private static final String	SORT_COLUMN			= GAME_SAVE_DATE;
		public static final String	DEFAULT_SORT_ORDER	= SORT_COLUMN + "DESC";

		public static final String	ALPHABETICALLY		= GAME_NAME;
		public static final String	OLDEST_FIRST		= GAME_START_DATE;

		private static final String	DEFAULT_INDEX		= "default";

		public static final String	CREATE_TABLE_CMD	= String.format(SQLiteStatements.CREATE_TABLE_CMD, TABLE_NAME, COLUMN_DEFINITION);

		public static final String	DROP_TABLE_CMD		= String.format(SQLiteStatements.DROP_TABLE_CMD, TABLE_NAME);

		public static final String	CREATE_INDEX_CMD	= String.format(SQLiteStatements.CREATE_INDEX_CMD, DEFAULT_INDEX, TABLE_NAME, SORT_COLUMN);
	}
}
