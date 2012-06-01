
package ua.org.antidotcb.dominus;


import ua.org.antidotcb.dominus.engine.DefaultUniverseBuilder;
import ua.org.antidotcb.dominus.engine.Engine;
import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;


public class PrimaryActivity
	extends Activity {

	private static final String	TAG	= PrimaryActivity.class.getName();

	@Override public void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "onCreate called");
		super.onCreate(savedInstanceState);

		engine = Engine.Instance();
		engine.CreateNew(new DefaultUniverseBuilder());

		setContentView(R.layout.main);
		universeView = (GLSurfaceView) findViewById(R.id.universeView);
	}

	@Override protected void onPause() {
		Log.i(TAG, "onPause called");
		super.onPause();
		universeView.onPause();
	}

	@Override protected void onResume() {
		Log.i(TAG, "onResume called");
		super.onResume();
		universeView.onResume();
	}

	@Override protected void onDestroy() {
		Log.i(TAG, "onDestroy called");
		super.onDestroy();
	}

	private GLSurfaceView	universeView;
	private Engine			engine;
}
