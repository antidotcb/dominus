package ua.org.antidotcb.dominus.engine;

import ua.org.antidotcb.dominus.model.GameProviderMetaData.GameTableMetaData;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;

public class GameDatabaseLoader implements GameDataProvider {

	private Cursor	cursor;

	public GameDatabaseLoader(Context content, Uri initialUri) {
		ContentResolver cr = content.getContentResolver();
		Uri uri = GameTableMetaData.getInstance().getContentUri();
		if (initialUri != null) {
			uri = initialUri;
		}
		cursor = cr.query(uri, null, null, null, null);
	}

	public void Close() {
		cursor.close();
	}

	public ArrayList<Race> getRaces() {
		return null;
	}

	public Universe getUniverse() {
		// TODO Auto-generated method stub
		return null;
	}

	public void Save() {
		// TODO Auto-generated method stub

	}

}
