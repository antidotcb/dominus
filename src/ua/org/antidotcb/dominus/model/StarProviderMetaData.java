
package ua.org.antidotcb.dominus.model;


import ua.org.antidotcb.dominus.model.SQLiteStatements.ColumnType;


public class StarProviderMetaData
	extends DominusProviderMetaData {

	public static final class StarTableMetaData
		extends BaseTableMetaData {

		private static volatile StarTableMetaData	_instance;

		// Columns definition begin
		public static final String					COLUMN_COORD_X	= "x";
		public static final String					COLUMN_COORD_Y	= "y";
		public static final String					COLUMN_COORD_Z	= "z";
		public static final String					COLUMN_GAME		= "game";
		public static final String					COLUMN_NAME		= "name";

		// Columns definition end

		public static StarTableMetaData getInstance() {
			if (_instance == null) {
				synchronized (StarTableMetaData.class) {
					if (_instance == null) {
						_instance = new StarTableMetaData();
					}
				}
			}
			return _instance;
		}

		private StarTableMetaData() {
			super();
		}

		@Override public final String getContentIdentity() {
			return "star";
		}

		@Override public String getDefaultSortColumn() {
			return COLUMN_NAME;
		}

		@Override protected void populateColumns() {
			addColumn(_ID, ColumnType.INTEGER);
			addColumn(COLUMN_NAME, ColumnType.TEXT);
			addColumn(COLUMN_GAME, ColumnType.INTEGER);
			addColumn(COLUMN_COORD_X, ColumnType.REAL);
			addColumn(COLUMN_COORD_X, ColumnType.REAL);
			addColumn(COLUMN_COORD_X, ColumnType.REAL);
		}
	}

	private StarProviderMetaData() {}
}
