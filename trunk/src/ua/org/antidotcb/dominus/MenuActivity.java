
package ua.org.antidotcb.dominus;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MenuActivity
	extends Activity {

	private static final String	TAG	= MenuActivity.class.getName();

	static {
		try {
			//System.loadLibrary("dominus");
		}
		catch (UnsatisfiedLinkError e) {
			Log.d("ERROR", "UnsatisfiedLinkError", e);
			System.exit(-1);
		}
		catch (Exception e) {
			Log.d("ERROR", "Exception", e);
			System.exit(-1);
		}
	}

	private void showButtonLabel(View v) {
		Button btn = (Button) v;
		if (btn == null) return;
		Toast toast = Toast.makeText(this, btn.getText().toString(), Toast.LENGTH_SHORT);
		toast.show();
	}

	public void doAbout(View v) {
		Log.i(TAG, "About clicked");
		String action = getResources().getString(R.string.intent_about);
		Intent intent = new Intent(action);
		this.startActivity(intent);
	}

	public void doExitGame(View v) {
		Log.i(TAG, "ExitGame clicked");
		this.finish();
	}

	public void doLoadGame(View v) {
		Log.i(TAG, "LoadGame clicked");
		// String action = getResources().getString(R.string.intent_loadgame);
		// Intent intent = new Intent(action);
		// this.startActivity(intent);
		showButtonLabel(v);
	}

	public void doNewGame(View v) {
		Log.i(TAG, "NewGame clicked");
		String action = getResources().getString(R.string.intent_newgame);
		Intent intent = new Intent(action);
		this.startActivity(intent);
	}

	public void doOptions(View v) {
		Log.i(TAG, "Options clicked");
		// String action = getResources().getString(R.string.intent_options);
		// Intent intent = new Intent(action);
		// this.startActivity(intent);
		showButtonLabel(v);
	}

	public void doSaveGame(View v) {
		Log.i(TAG, "SaveGame clicked");
		// String action = getResources().getString(R.string.intent_savegame);
		// Intent intent = new Intent(action);
		// this.startActivity(intent);
		showButtonLabel(v);
	}

	@Override public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainmenu);
	}
}
