package ua.org.antidotcb.dominus.model;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class GameProviderMetaData {

	public static final class GameTableMetaData implements BaseColumns {

		public static final String		_TABLE_NAME			= "games";

		// Columns definition begin

		public static final String		COLUMN_CREATED		= "created";
		public static final String		COLUMN_LENGTH		= "turns";
		public static final String		COLUMN_MODIFIED		= "modified";
		public static final String		COLUMN_NAME			= "name";
		public static final String		COLUMN_PLAYERID		= "player";

		// @formatter:off
		private static final String COLUMNS_DEFINITION = 
				  _ID				+ " INTEGER PRIMARY KEY AUTOINCREMENT," 
				+ COLUMN_NAME		+ " TEXT," 
				+ COLUMN_MODIFIED	+ " INTEGER,"
				+ COLUMN_CREATED	+ " INTEGER,"
				+ COLUMN_LENGTH		+ " INTEGER,"
				+ COLUMN_PLAYERID	+ " INTEGER";
		// @formatter:on
		// Columns definition end
		public static final String		CONTENT_ITEM_TYPE	= ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.antidotcb.dominus.game";
		public static final String		CONTENT_TYPE		= ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.antidotcb.dominus.game";
		public static final Uri			CONTENT_URI			= Uri.parse("content://" + AUTHORITY);

		private static final String		DEFAULT_INDEX		= "default";

		private static final String		SORT_COLUMN			= COLUMN_MODIFIED;

		public static final String		SORT_DEFAULT		= SORT_COLUMN + "DESC";

		protected static final String	SQL_CREATE_INDEX	= String.format(SQLiteStatements.CREATE_INDEX_CMD, DEFAULT_INDEX, _TABLE_NAME, SORT_COLUMN);

		protected static final String	SQL_CREATE_TABLE	= String.format(SQLiteStatements.CREATE_TABLE_CMD, _TABLE_NAME, COLUMNS_DEFINITION);

		protected static final String	SQL_DROP_TABLE		= String.format(SQLiteStatements.DROP_TABLE_CMD, _TABLE_NAME);

		private GameTableMetaData() {}
	}

	public static final String		AUTHORITY			= "ua.org.antidotcb.dominus.model.GameProvider";
	protected static final String	DATABASE_NAME		= "game.db";
	protected static final int		DATABASE_VERSION	= 2;

	private GameProviderMetaData() {}
}
