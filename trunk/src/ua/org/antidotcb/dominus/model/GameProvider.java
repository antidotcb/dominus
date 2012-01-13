package ua.org.antidotcb.dominus.model;

import ua.org.antidotcb.dominus.model.GameProviderMetaData.GameTableMetaData;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;

public class GameProvider extends ContentProvider {

	private static final String				FAIL_INSERT_ROW_ERROR						= "Failed to insert row into %s";

	private static final String				EMPTY_REQUIRED_FIELD_ERROR					= "Failed to insert row because %d is required field.";

	private static final String				QUERY_RETURN_NULL_WARNING					= "Query return empty cursor";

	private static final String				UNKNOWN_URI_ERROR							= "Unknown URI %s";

	// helper TAG for logging
	private static final String				TAG											= GameProvider.class.getName();

	private static HashMap<String, String>	sGamesProjectionMap;
	static {
		sGamesProjectionMap = new HashMap<String, String>();

		sGamesProjectionMap.put(BaseColumns._ID, BaseColumns._ID);

		sGamesProjectionMap.put(GameTableMetaData.GAME_NAME, GameTableMetaData.GAME_NAME);

		sGamesProjectionMap.put(GameTableMetaData.GAME_SAVE_DATE, GameTableMetaData.GAME_SAVE_DATE);

		sGamesProjectionMap.put(GameTableMetaData.GAME_TURNS, GameTableMetaData.GAME_TURNS);

		sGamesProjectionMap.put(GameTableMetaData.GAME_START_DATE, GameTableMetaData.GAME_START_DATE);

		sGamesProjectionMap.put(GameTableMetaData.GAME_PLAYER, GameTableMetaData.GAME_PLAYER);
	}

	private static final UriMatcher			sUriMatcher;
	private static final int				INCOMINING_GAME_COLLECTION_URI_INDICATOR	= 1;
	private static final int				INCOMINING_SINGLE_GAME_URI_INDICATOR		= 2;

	static {
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		sUriMatcher.addURI(GameProviderMetaData.AUTHORITY, GameTableMetaData.TABLE_NAME, INCOMINING_GAME_COLLECTION_URI_INDICATOR);

		sUriMatcher.addURI(GameProviderMetaData.AUTHORITY, GameTableMetaData.TABLE_NAME + "/#", INCOMINING_SINGLE_GAME_URI_INDICATOR);
	}

	private static class GameDatabaseHelper extends SQLiteOpenHelper {

		private static final String	UPGRADE_INTERNAL_DATABASE	= "Upgrade internal database from %d to %d - all old data are removed.";

		public GameDatabaseHelper(Context context) {
			super(context, GameProviderMetaData.DATABASE_NAME, null, GameProviderMetaData.DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			Log.d(TAG, "Inner onCreate called");

			final String createTableCmd = GameTableMetaData.CREATE_TABLE_CMD;
			final String createIndexCmd = GameTableMetaData.CREATE_INDEX_CMD;
			try {
				db.execSQL(createTableCmd);

				try {
					db.execSQL(createIndexCmd);
				} catch (SQLException e) {
					Log.e(TAG, createIndexCmd, e);
				}
			} catch (SQLException e) {
				Log.e(TAG, createTableCmd, e);
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.d(TAG, "Inner onUpgrade called");

			Log.w(TAG, String.format(GameDatabaseHelper.UPGRADE_INTERNAL_DATABASE, oldVersion, newVersion));

			final String dropTableCmd = GameTableMetaData.DROP_TABLE_CMD;
			try {
				db.execSQL(dropTableCmd);
			} catch (SQLException e) {
				Log.e(TAG, dropTableCmd, e);
			}
		}

	}

	@Override
	public int delete(Uri uri, String initialSelection, String[] selectionArgs) {
		SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();

		int count;

		switch (sUriMatcher.match(uri)) {
		case INCOMINING_GAME_COLLECTION_URI_INDICATOR:
			count = db.delete(GameTableMetaData.TABLE_NAME, initialSelection, selectionArgs);
			break;

		case INCOMINING_SINGLE_GAME_URI_INDICATOR:
			String rowId = uri.getPathSegments().get(1);
			String selection = BaseColumns._ID + "=" + rowId;
			if (TextUtils.isEmpty(initialSelection) == false) {
				selection += " AND " + initialSelection;
			}

			count = db.delete(GameTableMetaData.TABLE_NAME, selection, selectionArgs);
			break;

		default:
			throw new IllegalArgumentException(String.format(GameProvider.UNKNOWN_URI_ERROR, uri));
		}

		getContext().getContentResolver().notifyChange(uri, null);

		return count;
	}

	@Override
	public String getType(Uri uri) {
		switch (sUriMatcher.match(uri)) {
		case INCOMINING_GAME_COLLECTION_URI_INDICATOR:
			return GameTableMetaData.CONTENT_TYPE;

		case INCOMINING_SINGLE_GAME_URI_INDICATOR:
			return GameTableMetaData.CONTENT_ITEM_TYPE;

		default:
			throw new IllegalArgumentException(String.format(GameProvider.UNKNOWN_URI_ERROR, uri));
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues initialValues) {
		if (sUriMatcher.match(uri) != INCOMINING_GAME_COLLECTION_URI_INDICATOR) {
			throw new IllegalArgumentException(String.format(GameProvider.UNKNOWN_URI_ERROR, uri));
		}

		ContentValues values;
		if (initialValues != null) {
			values = new ContentValues(initialValues);
		} else {
			values = initialValues;
		}

		Long now = Long.valueOf(System.currentTimeMillis());

		if (values.containsKey(GameTableMetaData.GAME_START_DATE)) {
			values.remove(GameTableMetaData.GAME_START_DATE);
		}

		values.put(GameTableMetaData.GAME_START_DATE, now);

		if (values.containsKey(GameTableMetaData.GAME_SAVE_DATE)) {
			values.remove(GameTableMetaData.GAME_SAVE_DATE);
		}

		values.put(GameTableMetaData.GAME_SAVE_DATE, now);

		if (values.containsKey(GameTableMetaData.GAME_TURNS)) {
			values.remove(GameTableMetaData.GAME_TURNS);
		}

		values.put(GameTableMetaData.GAME_TURNS, 0);

		if (values.containsKey(GameTableMetaData.GAME_NAME) == false) {
			throw new SQLException(String.format(GameProvider.EMPTY_REQUIRED_FIELD_ERROR, GameTableMetaData.GAME_NAME));
		}

		if (values.containsKey(GameTableMetaData.GAME_PLAYER) == false) {
			throw new SQLException(String.format(GameProvider.EMPTY_REQUIRED_FIELD_ERROR, GameTableMetaData.GAME_PLAYER));
		}

		SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();

		long rowId = db.insertOrThrow(GameTableMetaData.TABLE_NAME, GameTableMetaData.GAME_NAME, values);

		if (rowId > 0) {
			Uri insertedGameUri = ContentUris.withAppendedId(GameTableMetaData.CONTENT_URI, rowId);

			getContext().getContentResolver().notifyChange(insertedGameUri, null);

			return insertedGameUri;
		}

		throw new SQLException(String.format(GameProvider.FAIL_INSERT_ROW_ERROR, uri));
	}

	private GameDatabaseHelper	mDatabaseHelper;

	@Override
	public boolean onCreate() {
		Log.d(TAG, "onCreate called");
		mDatabaseHelper = new GameDatabaseHelper(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		qb.setTables(GameTableMetaData.TABLE_NAME);
		qb.setProjectionMap(sGamesProjectionMap);

		switch (sUriMatcher.match(uri)) {
		case INCOMINING_GAME_COLLECTION_URI_INDICATOR:
			// Default query
			break;
		case INCOMINING_SINGLE_GAME_URI_INDICATOR:
			qb.appendWhere(BaseColumns._ID + "=" + uri.getPathSegments().get(1));
			break;
		default:
			throw new IllegalArgumentException(String.format(GameProvider.UNKNOWN_URI_ERROR, uri));
		}

		String orderBy = GameTableMetaData.DEFAULT_SORT_ORDER;
		if (TextUtils.isEmpty(sortOrder)) {
			orderBy = sortOrder;
		}

		SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();

		Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, orderBy);

		if (c != null) {
			c.setNotificationUri(getContext().getContentResolver(), uri);
		} else {
			Log.w(TAG, GameProvider.QUERY_RETURN_NULL_WARNING);
		}
		return c;
	}

	@Override
	public int update(Uri uri, ContentValues values, String initialSelection, String[] selectionArgs) {
		SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();

		int count;

		if (values.containsKey(GameTableMetaData.GAME_SAVE_DATE)) {
			values.remove(GameTableMetaData.GAME_SAVE_DATE);
		}

		Long now = Long.valueOf(System.currentTimeMillis());
		values.put(GameTableMetaData.GAME_SAVE_DATE, now);

		switch (sUriMatcher.match(uri)) {
		case INCOMINING_GAME_COLLECTION_URI_INDICATOR:
			count = db.update(GameTableMetaData.TABLE_NAME, values, initialSelection, selectionArgs);
			break;

		case INCOMINING_SINGLE_GAME_URI_INDICATOR:
			String rowId = uri.getPathSegments().get(1);
			String selection = BaseColumns._ID + "=" + rowId;
			if (TextUtils.isEmpty(initialSelection) == false) {
				selection += " AND " + initialSelection;
			}

			count = db.update(GameTableMetaData.TABLE_NAME, values, selection, selectionArgs);
			break;

		default:
			throw new IllegalArgumentException(String.format(GameProvider.UNKNOWN_URI_ERROR, uri));
		}

		getContext().getContentResolver().notifyChange(uri, null);

		return count;
	}
}
