
package ua.org.antidotcb.dominus;


import ua.org.antidotcb.dominus.model.GameProviderMetaData.GameTableMetaData;
import android.app.Activity;
import android.content.CursorLoader;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


public class LoadActivity
	extends Activity {

	@SuppressWarnings("unused") private static final String	TAG	= LoadActivity.class.getName();

	@Override public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.loadgame);

		GameTableMetaData games = GameTableMetaData.getInstance();

		CursorLoader cursor_loader = new CursorLoader(this, games.getContentUri(), null, null, null, null);

		String[] cols = new String[] { GameTableMetaData.COLUMN_NAME };

		int[] views = new int[] { android.R.id.text1 };

		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, cursor_loader.loadInBackground(), cols, views, 0);
		// SimpleCursorAdapter(this, android.R.layout.simple_list_item_1,
		// cursor, cols, views);

		ListView list = (ListView) findViewById(R.id.listView1);
		list.setAdapter(adapter);
		// this.setListAdapter(adapter);
	}
}
