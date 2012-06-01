
package ua.org.antidotcb.dominus.graphics;


import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Vector;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;


public class ObjRenderer
	extends AbstractRenderer {

	private Vector<Model>	models;
	private Bitmap			bitmap;

	public ObjRenderer(Context context, String fileName) {
		AssetManager manager = context.getAssets();
		try {
			BufferedInputStream inputStream = new BufferedInputStream(manager.open(fileName));
			models = (new ObjParser()).parseStream(inputStream);

			inputStream = new BufferedInputStream(manager.open("earthmap2.png"));
			bitmap = BitmapFactory.decodeStream(inputStream);
		}
		catch (IOException e) {
			Log.e(TAG, e.getMessage());
		}
	}

	private boolean	loaded	= false;

	@Override protected void draw(GL10 gl) {
		if (models != null) {
			if (!loaded) {
				loaded = true;
				Model.loadTexture(gl, bitmap);
			}
			for (Model model : models)
				model.render(gl);
		}
	}

	private static final String	TAG	= ObjRenderer.class.getName();

}
