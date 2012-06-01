
package ua.org.antidotcb.dominus.model;


import java.util.HashMap;

import ua.org.antidotcb.dominus.model.SQLiteStatements.ColumnType;
import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;


public class DominusProviderMetaData {

	protected static abstract class BaseTableMetaData
		implements BaseColumns, MetaData {

		protected static final String		DEFAULT_INDEX	= "default";

		private HashMap<String, ColumnType>	columnsTypes;

		protected BaseTableMetaData() {
			columnsTypes = new HashMap<String, ColumnType>();
			populateColumns();
		}

		protected final void addColumn(final String name, final ColumnType type) {
			columnsTypes.put(name, type);
		}

		public final String getColumn(int columnID) {
			Object[] keys = columnsTypes.keySet().toArray();
			return (String) keys[columnID];
		}

		public final int getColumnsCount() {
			return columnsTypes.size();
		}

		protected final String getColumnsDefinition() {
			StringBuilder sb = new StringBuilder();
			for (int column = 0; column < getColumnsCount(); column++) {
				sb.append(getColumn(column));
				sb.append(" ");
				sb.append(SQLiteStatements.getTypeString(getColumnType(column)));
				int isPrimary = getColumn(column).compareToIgnoreCase(BaseColumns._ID);
				if (isPrimary == 0) {
					sb.append(" ");
					sb.append(SQLiteStatements.PRIMARY_KEY);
					sb.append(" ");
					sb.append(SQLiteStatements.AUTOINCREMENT);
				}
				sb.append(",");
			}
			sb.setLength(sb.length() - 1);
			return sb.toString();
		}

		public final ColumnType getColumnType(int columnID) {
			Object[] keys = columnsTypes.keySet().toArray();
			return columnsTypes.get(keys[columnID]);
		}

		public abstract String getContentIdentity();

		public final String getContentItemType() {
			return ContentResolver.CURSOR_ITEM_BASE_TYPE + PRE_VND_ANTIDOTCB_DOMINUS + getContentIdentity();
		}

		public final String getContentType() {
			return ContentResolver.CURSOR_DIR_BASE_TYPE + PRE_VND_ANTIDOTCB_DOMINUS + getContentIdentity();
		}

		public final Uri getContentUri() {
			return Uri.withAppendedPath(CONTENT_URI_BASE, getTableName());
		}

		public String getDefaultSort() {
			return getDefaultSortColumn();
		}

		protected String getDefaultSortColumn() {
			return BaseColumns._ID;
		}

		public final String getSqlCreateIndex() {
			return String.format(SQLiteStatements.CREATE_INDEX_CMD, DEFAULT_INDEX, getTableName(), getDefaultSortColumn());
		}

		public final String getSqlCreateTable() {
			return String.format(SQLiteStatements.CREATE_TABLE_CMD, getTableName(), getColumnsDefinition());
		}

		public final String getSqlDropTable() {
			return String.format(SQLiteStatements.DROP_TABLE_CMD, getTableName());
		}

		public String getTableName() {
			return getContentIdentity() + "s";
		}

		protected abstract void populateColumns();
	}

	public static final String		AUTHORITY					= "ua.org.antidotcb.dominus.model.GameProvider";

	protected static final Uri		CONTENT_URI_BASE			= Uri.parse(String.format("%s://%s", ContentResolver.SCHEME_CONTENT, AUTHORITY));

	protected static final String	DATABASE_NAME				= "dominus.db";
	protected static final int		DATABASE_VERSION			= 4;

	protected static final String	BASE_VND_ANTIDOTCB_DOMINUS	= "vnd.antidotcb.dominus";
	protected static final String	PRE_VND_ANTIDOTCB_DOMINUS	= String.format("/%s.", BASE_VND_ANTIDOTCB_DOMINUS);
}
