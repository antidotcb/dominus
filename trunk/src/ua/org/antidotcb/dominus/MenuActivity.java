package ua.org.antidotcb.dominus;

import ua.org.antidotcb.dominus.engine.NativeQuatUtils;
import ua.org.antidotcb.dominus.engine.Quaternion;
import ua.org.antidotcb.dominus.model.GameProviderMetaData;
import ua.org.antidotcb.dominus.model.GameProviderMetaData.GameTableMetaData;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MenuActivity extends Activity implements OnClickListener {

	public void onClick(View v) {

		Quaternion q = new Quaternion();
		NativeQuatUtils.normalizeQuaternion(q);

		int id = v.getId();

		ContentValues values;
		ContentResolver cr = getContentResolver();
		Uri uri = Uri.withAppendedPath(
				GameProviderMetaData.GameTableMetaData.CONTENT_URI,
				GameTableMetaData._TABLE_NAME);
		final Button btn = (Button) findViewById(id);

		if (btn != null)
			Log.i(TAG, String.format("Button '%s' is pressed", btn.getText()));

		switch (id) {
		case R.id.mm_newgame:

			String action = getResources().getString(
					R.string.intent_displaymain);
			Intent intent = new Intent(action);
			this.startActivity(intent);

			values = new ContentValues();
			Uri insertedUri;

			values.put(GameTableMetaData.COLUMN_NAME, "The first one");
			values.put(GameTableMetaData.COLUMN_PLAYERID, 1);
			insertedUri = cr.insert(uri, values);
			Log.d(TAG, insertedUri.toString());

			values.put(GameTableMetaData.COLUMN_NAME, "Number two");
			values.put(GameTableMetaData.COLUMN_PLAYERID, 2);
			insertedUri = cr.insert(uri, values);
			Log.d(TAG, insertedUri.toString());

			values.put(GameTableMetaData.COLUMN_NAME, "Third one");
			values.put(GameTableMetaData.COLUMN_PLAYERID, 3);
			insertedUri = cr.insert(uri, values);
			Log.d(TAG, insertedUri.toString());

			values.put(GameTableMetaData.COLUMN_NAME, "Last try");
			values.put(GameTableMetaData.COLUMN_PLAYERID, 4);
			insertedUri = cr.insert(uri, values);
			Log.d(TAG, insertedUri.toString());

			break;

		case R.id.mm_loadgame:

			Cursor c = managedQuery(uri, null, null, null, null);

			int iname = c.getColumnIndex(GameTableMetaData.COLUMN_NAME);
			int iplayer = c.getColumnIndex(GameTableMetaData.COLUMN_PLAYERID);
			int imodified = c.getColumnIndex(GameTableMetaData.COLUMN_MODIFIED);
			int istart = c.getColumnIndex(GameTableMetaData.COLUMN_CREATED);
			int iturns = c.getColumnIndex(GameTableMetaData.COLUMN_LENGTH);

			Log.d(TAG, "iname =" + iname);
			Log.d(TAG, "iplayer =" + iplayer);
			Log.d(TAG, "imodified =" + imodified);
			Log.d(TAG, "istart =" + istart);
			Log.d(TAG, "iturns =" + iturns);

			StringBuffer sbuf = new StringBuffer("Output:\n");

			for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
				String cid = c.getString(1);
				String name = c.getString(iname);
				String player = c.getString(iplayer);
				String start = c.getString(istart);
				String modified = c.getString(imodified);
				String turns = c.getString(iturns);

				sbuf.append(cid).append(',');
				sbuf.append(name).append(',');
				sbuf.append(player).append(',');
				sbuf.append(start).append(',');
				sbuf.append(modified).append(',');
				sbuf.append(turns).append('\n');
			}

			Context context = getApplicationContext();
			CharSequence text = sbuf.toString();
			int duration = Toast.LENGTH_SHORT;

			Toast toast = Toast.makeText(context, text, duration);
			toast.show();

			break;

		case R.id.mm_savegame:

			values = new ContentValues();

			values.put(GameTableMetaData.COLUMN_PLAYERID, 5);

			String where = GameTableMetaData._ID + "=?";
			String[] selectionArgs = new String[] { "3" };
			cr.update(uri, values, where, selectionArgs);

			break;

		case R.id.mm_exitgame:
			cr.delete(uri, null, null);
			this.finish();
			break;
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.mainmenu);

		Button btn;

		btn = (Button) findViewById(R.id.mm_newgame);
		btn.setOnClickListener(this);

		btn = (Button) findViewById(R.id.mm_loadgame);
		btn.setOnClickListener(this);

		btn = (Button) findViewById(R.id.mm_savegame);
		btn.setOnClickListener(this);

		btn = (Button) findViewById(R.id.mm_exitgame);
		btn.setOnClickListener(this);
	}

	private static final String TAG = MenuActivity.class.getName();

	static {
		try {
			System.loadLibrary("quaternion");
		} catch (UnsatisfiedLinkError e) {
			Log.d("ERROR", "UnsatisfiedLinkError", e);
			System.exit(-1);
		} catch (Exception e) {
			Log.d("ERROR", "Exception", e);
			System.exit(-1);
		}
	}
}
