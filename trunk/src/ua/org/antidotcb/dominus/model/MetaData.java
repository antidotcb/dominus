package ua.org.antidotcb.dominus.model;

import ua.org.antidotcb.dominus.model.SQLiteStatements.ColumnType;

import android.net.Uri;

public interface MetaData {

	abstract String getColumn(int columnID);

	abstract int getColumnsCount();

	abstract ColumnType getColumnType(int columnID);

	abstract String getContentIdentity();

	abstract String getContentItemType();

	abstract String getContentType();

	abstract Uri getContentUri();

	abstract String getDefaultSort();

	abstract String getSqlCreateIndex();

	abstract String getSqlCreateTable();

	abstract String getSqlDropTable();

	public abstract String getTableName();
}
