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
import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;

public class GameProvider extends ContentProvider {

	private static class GameDatabaseHelper extends SQLiteOpenHelper {

		public GameDatabaseHelper(Context context) {
			super(context, GameProviderMetaData.DATABASE_NAME, null, GameProviderMetaData.DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			Log.d(_TAG, "Inner onCreate called");

			final String createTableCmd = GameTableMetaData.SQL_CREATE_TABLE;
			final String createIndexCmd = GameTableMetaData.SQL_CREATE_INDEX;
			try {
				db.execSQL(createTableCmd);

				try {
					db.execSQL(createIndexCmd);
				} catch (SQLException e) {
					Log.e(_TAG, createIndexCmd, e);
				}
			} catch (SQLException e) {
				Log.e(_TAG, createTableCmd, e);
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.d(_TAG, "Inner onUpgrade called");

			Log.w(_TAG, String.format(GameDatabaseHelper.UPGRADE_INTERNAL_DATABASE, oldVersion, newVersion));

			final String dropTableCmd = GameTableMetaData.SQL_DROP_TABLE;
			try {
				db.execSQL(dropTableCmd);
			} catch (SQLException e) {
				Log.e(_TAG, dropTableCmd, e);
			}
		}

		private static final String	UPGRADE_INTERNAL_DATABASE	= "Upgrade internal database from %d to %d - all old data are removed.";

	}

	@Override
	public int delete(Uri uri, String initialSelection, String[] selectionArgs) {
		SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();

		int count;

		switch (sMatcher.match(uri)) {
		case FORMAT_GAME_COLLECTION_URI:
			count = db.delete(GameTableMetaData._TABLE_NAME, initialSelection, selectionArgs);
			break;

		case FORMAT_SINGLE_GAME_URI:
			String rowId = uri.getPathSegments().get(1);
			String selection = GameTableMetaData._ID + "=" + rowId;
			if (TextUtils.isEmpty(initialSelection) == false) {
				selection += " AND " + initialSelection;
			}

			count = db.delete(GameTableMetaData._TABLE_NAME, selection, selectionArgs);
			break;

		default:
			throw new IllegalArgumentException(String.format(GameProvider.STR_ERR_UNKNOWN_URI, uri));
		}

		getContext().getContentResolver().notifyChange(uri, null);

		return count;
	}

	@Override
	public String getType(Uri uri) {
		switch (sMatcher.match(uri)) {
		case FORMAT_GAME_COLLECTION_URI:
			return GameTableMetaData.CONTENT_TYPE;

		case FORMAT_SINGLE_GAME_URI:
			return GameTableMetaData.CONTENT_ITEM_TYPE;

		default:
			throw new IllegalArgumentException(String.format(GameProvider.STR_ERR_UNKNOWN_URI, uri));
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues initialValues) {
		if (sMatcher.match(uri) != FORMAT_GAME_COLLECTION_URI) {
			throw new IllegalArgumentException(String.format(GameProvider.STR_ERR_UNKNOWN_URI, uri));
		}

		ContentValues values;
		if (initialValues != null) {
			values = new ContentValues(initialValues);
		} else {
			values = initialValues;
		}

		Long now = Long.valueOf(System.currentTimeMillis());

		if (values.containsKey(GameTableMetaData.COLUMN_CREATED)) {
			values.remove(GameTableMetaData.COLUMN_CREATED);
		}

		values.put(GameTableMetaData.COLUMN_CREATED, now);

		if (values.containsKey(GameTableMetaData.COLUMN_MODIFIED)) {
			values.remove(GameTableMetaData.COLUMN_MODIFIED);
		}

		values.put(GameTableMetaData.COLUMN_MODIFIED, now);

		if (values.containsKey(GameTableMetaData.COLUMN_LENGTH)) {
			values.remove(GameTableMetaData.COLUMN_LENGTH);
		}

		values.put(GameTableMetaData.COLUMN_LENGTH, 0);

		if (values.containsKey(GameTableMetaData.COLUMN_NAME) == false) {
			throw new SQLException(String.format(GameProvider.STR_ERR_EMPTY_REQUIRED_FIELD, GameTableMetaData.COLUMN_NAME));
		}

		if (values.containsKey(GameTableMetaData.COLUMN_PLAYERID) == false) {
			throw new SQLException(String.format(GameProvider.STR_ERR_EMPTY_REQUIRED_FIELD, GameTableMetaData.COLUMN_PLAYERID));
		}

		SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();

		long rowId = db.insertOrThrow(GameTableMetaData._TABLE_NAME, GameTableMetaData.COLUMN_NAME, values);

		if (rowId > 0) {
			Uri insertedGameUri = ContentUris.withAppendedId(GameTableMetaData.CONTENT_URI, rowId);

			getContext().getContentResolver().notifyChange(insertedGameUri, null);

			return insertedGameUri;
		}

		throw new SQLException(String.format(GameProvider.STR_ERR_FAIL_INSERT_ROW, uri));
	}

	@Override
	public boolean onCreate() {
		Log.d(_TAG, "onCreate called");
		mDatabaseHelper = new GameDatabaseHelper(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		qb.setTables(GameTableMetaData._TABLE_NAME);
		qb.setProjectionMap(sGamesProjection);

		switch (sMatcher.match(uri)) {
		case FORMAT_GAME_COLLECTION_URI:
			// Default query
			break;
		case FORMAT_SINGLE_GAME_URI:
			qb.appendWhere(GameTableMetaData._ID + "=" + uri.getPathSegments().get(1));
			break;
		default:
			throw new IllegalArgumentException(String.format(GameProvider.STR_ERR_UNKNOWN_URI, uri));
		}

		String orderBy = GameTableMetaData.SORT_DEFAULT;
		if (TextUtils.isEmpty(sortOrder)) {
			orderBy = sortOrder;
		}

		SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();

		Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, orderBy);

		if (c != null) {
			c.setNotificationUri(getContext().getContentResolver(), uri);
		} else {
			Log.w(_TAG, GameProvider.STR_WANR_QUERY_RETURN_NULL);
		}
		return c;
	}

	@Override
	public int update(Uri uri, ContentValues values, String initialSelection, String[] selectionArgs) {
		SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();

		int count;

		if (values.containsKey(GameTableMetaData.COLUMN_MODIFIED)) {
			values.remove(GameTableMetaData.COLUMN_MODIFIED);
		}

		Long now = Long.valueOf(System.currentTimeMillis());
		values.put(GameTableMetaData.COLUMN_MODIFIED, now);

		switch (sMatcher.match(uri)) {
		case FORMAT_GAME_COLLECTION_URI:
			count = db.update(GameTableMetaData._TABLE_NAME, values, initialSelection, selectionArgs);
			break;

		case FORMAT_SINGLE_GAME_URI:
			String rowId = uri.getPathSegments().get(1);
			String selection = GameTableMetaData._ID + "=" + rowId;
			if (TextUtils.isEmpty(initialSelection) == false) {
				selection += " AND " + initialSelection;
			}

			count = db.update(GameTableMetaData._TABLE_NAME, values, selection, selectionArgs);
			break;

		default:
			throw new IllegalArgumentException(String.format(GameProvider.STR_ERR_UNKNOWN_URI, uri));
		}

		getContext().getContentResolver().notifyChange(uri, null);

		return count;
	}

	// helper TAG for logging
	private static final String				_TAG							= GameProvider.class.getName();
	private static final int				FORMAT_GAME_COLLECTION_URI		= 1;
	private static final int				FORMAT_SINGLE_GAME_URI			= 2;
	
	private static HashMap<String, String>	sGamesProjection;
	private static final UriMatcher			sMatcher;
	
	private static final String				STR_ERR_EMPTY_REQUIRED_FIELD	= "Failed to insert row because %d is required field.";
	private static final String				STR_ERR_FAIL_INSERT_ROW			= "Failed to insert row into %s";
	private static final String				STR_ERR_UNKNOWN_URI				= "Unknown URI %s";
	private static final String				STR_WANR_QUERY_RETURN_NULL		= "Query return empty cursor";

	static {
		sGamesProjection = new HashMap<String, String>();

		sGamesProjection.put(GameTableMetaData._ID, GameTableMetaData._ID);
		sGamesProjection.put(GameTableMetaData.COLUMN_NAME, GameTableMetaData.COLUMN_NAME);
		sGamesProjection.put(GameTableMetaData.COLUMN_MODIFIED, GameTableMetaData.COLUMN_MODIFIED);
		sGamesProjection.put(GameTableMetaData.COLUMN_LENGTH, GameTableMetaData.COLUMN_LENGTH);
		sGamesProjection.put(GameTableMetaData.COLUMN_CREATED, GameTableMetaData.COLUMN_CREATED);
		sGamesProjection.put(GameTableMetaData.COLUMN_PLAYERID, GameTableMetaData.COLUMN_PLAYERID);

		sMatcher = new UriMatcher(UriMatcher.NO_MATCH);

		sMatcher.addURI(GameProviderMetaData.AUTHORITY, GameTableMetaData._TABLE_NAME, FORMAT_GAME_COLLECTION_URI);
		sMatcher.addURI(GameProviderMetaData.AUTHORITY, GameTableMetaData._TABLE_NAME + "/#", FORMAT_SINGLE_GAME_URI);
	}

	private GameDatabaseHelper				mDatabaseHelper;
}
