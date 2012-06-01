
package ua.org.antidotcb.dominus.model;


public class SQLiteStatements {

	private SQLiteStatements() {}

	public static final String	PRIMARY_KEY		= "PRIMARY KEY";
	public static final String	AUTOINCREMENT	= "AUTOINCREMENT";
	public static final String	DESC			= "DESC";

	private final static String	TYPE_BLOB		= "BLOB";
	private final static String	TYPE_INTEGER	= "INTEGER";
	private final static String	TYPE_REAL		= "REAL";
	private final static String	TYPE_TEXT		= "TEXT";

	public enum ColumnType {
		BLOB, INTEGER, REAL, TEXT
	}

	public static final String getTypeString(ColumnType type) {
		switch (type) {
		case BLOB:
			return TYPE_BLOB;
		case INTEGER:
			return TYPE_INTEGER;
		case REAL:
			return TYPE_REAL;
		case TEXT:
			return TYPE_TEXT;
		}

		return null;
	}

	public static final String	CREATE_INDEX_CMD	= "CREATE INDEX %s ON %s ( %s );";
	public static final String	CREATE_TABLE_CMD	= "CREATE TABLE %s ( %s );";
	public static final String	DROP_TABLE_CMD		= "DROP TABLE IF EXIST %s";

}
