package ua.org.antidotcb.dominus;

import ua.org.antidotcb.dominus.engine.DefaultUniverseBuilder;
import ua.org.antidotcb.dominus.engine.Engine;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

public class PrimaryActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		engine = Engine.Instance();
		engine.CreateNew(new DefaultUniverseBuilder());

		setContentView(R.layout.main);
		universeView = (GLSurfaceView) findViewById(R.id.universeView);
	}

	@Override
	protected void onPause() {
		super.onPause();
		universeView.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		universeView.onResume();
	}

	private GLSurfaceView	universeView;
	private Engine			engine;
}