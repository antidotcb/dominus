
package ua.org.antidotcb.dominus;


import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;


public class NewGameActivity
	extends Activity {

	private static final String	TAG	= NewGameActivity.class.getName();

	private Integer				density;

	public void onBack(View v) {
		finish();
	}

	@Override public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newgame);
		density = 0;
	}

	public void onDensityChange(View v) {
		Log.i(TAG, "Density clicked");

		if (++density > 4) density = 0;

		updateDensityPicture();
	}

	private void updateDensityPicture() {
		final int[] densityId = { R.drawable.ng_density0, R.drawable.ng_density1, R.drawable.ng_density2, R.drawable.ng_density3, R.drawable.ng_density4 };

		ImageButton densityButton = (ImageButton) findViewById(R.id.ng_density);
		if (densityButton != null) {
			densityButton.setImageResource(densityId[density]);
		}
	}

	public void onNext(View v) {
		Log.i(TAG, "Next clicked");

		String action = getResources().getString(R.string.intent_main);
		Intent intent = new Intent(action);

		intent.putExtra("new", true);
		intent.putExtra("density", density);

		this.startActivity(intent);
	}

	public void onRandomize(View v) {
		density = new Random().nextInt(5);
		updateDensityPicture();
	}
}
