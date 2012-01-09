package ua.org.antidotcb.dominus.model;

public class SQLiteStatements {

	private SQLiteStatements() {}

	public static final String	CREATE_INDEX_CMD	= "CREATE INDEX %s ON %s ( %s );";
	public static final String	CREATE_TABLE_CMD	= "CREATE TABLE %s ( %s );";
	public static final String	DROP_TABLE_CMD		= "DROP TABLE IF EXIST %s";

}
