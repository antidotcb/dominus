package ua.org.antidotcb.dominus.model;

import ua.org.antidotcb.dominus.model.SQLiteStatements.ColumnType;
import ua.org.antidotcb.dominus.model.StarProviderMetaData.StarTableMetaData;

public class GameProviderMetaData extends DominusProviderMetaData {

	public static final class GameTableMetaData extends BaseTableMetaData {

		private static volatile GameTableMetaData	_instance;

		// Columns definition begin

		public static final String					COLUMN_CREATED	= "created";
		public static final String					COLUMN_LENGTH	= "turns";
		public static final String					COLUMN_MODIFIED	= "modified";
		public static final String					COLUMN_NAME		= "name";
		public static final String					COLUMN_PLAYERID	= "player";

		// Columns definition end

		public static GameTableMetaData getInstance() {
			if (_instance == null) {
				synchronized (StarTableMetaData.class) {
					if (_instance == null) {
						_instance = new GameTableMetaData();
					}
				}
			}
			return _instance;
		}

		private GameTableMetaData() {
			super();
		}

		@Override
		public final String getContentIdentity() {
			return "game";
		}

		@Override
		public String getDefaultSort() {
			return super.getDefaultSort() + " " + SQLiteStatements.DESC;
		}

		@Override
		public String getDefaultSortColumn() {
			return COLUMN_MODIFIED;
		}

		@Override
		protected void populateColumns() {
			addColumn(_ID, ColumnType.INTEGER);
			addColumn(COLUMN_NAME, ColumnType.TEXT);
			addColumn(COLUMN_MODIFIED, ColumnType.INTEGER);
			addColumn(COLUMN_CREATED, ColumnType.INTEGER);
			addColumn(COLUMN_LENGTH, ColumnType.INTEGER);
			addColumn(COLUMN_PLAYERID, ColumnType.INTEGER);
		}
	}

	private GameProviderMetaData() {}
}
