
package ua.org.antidotcb.dominus.graphics;


import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.HashMap;

import javax.microedition.khronos.opengles.GL10;

import ua.org.antidotcb.dominus.engine.Engine;
import ua.org.antidotcb.dominus.engine.StarSystem;
import android.content.Context;
import android.graphics.Color;
import android.util.Pair;


public class DefaultUniverseRenderer
	extends AbstractRenderer {

	private final static int	RGBA		= 4;
	private final static int	X3COORDS	= 3;

	private FloatBuffer			colorBuffer;
	private ShortBuffer			linksIndicesBuffer;
	private int					linksVerticesCount;
	private ShortBuffer			starsIndicesBuffer;
	private int					starVerticesCount;
	private FloatBuffer			vertexBuffer;
	private Engine				engine;

	private boolean				needPrepare	= false;

	public DefaultUniverseRenderer(Context context, Engine engine) {
		this.engine = engine;
		prepareBuffers(engine);
	}

	private void prepareBuffers(Engine engine) {
		ArrayList<StarSystem> starSystems = engine.getUniverse().getStarSystems();
		starVerticesCount = starSystems.size();

		ByteBuffer nativeVertexBuffer = ByteBuffer.allocateDirect((starVerticesCount) * (Float.SIZE / Byte.SIZE) * X3COORDS);
		nativeVertexBuffer.order(ByteOrder.nativeOrder());
		vertexBuffer = nativeVertexBuffer.asFloatBuffer();

		ByteBuffer nativeColorBuffer = ByteBuffer.allocateDirect((starVerticesCount) * (Float.SIZE / Byte.SIZE) * RGBA);
		nativeColorBuffer.order(ByteOrder.nativeOrder());
		colorBuffer = nativeColorBuffer.asFloatBuffer();

		ByteBuffer nativeStarIndicesBuffer = ByteBuffer.allocateDirect((starVerticesCount) * (Short.SIZE / Byte.SIZE));
		nativeStarIndicesBuffer.order(ByteOrder.nativeOrder());
		starsIndicesBuffer = nativeStarIndicesBuffer.asShortBuffer();

		short index = 0;

		HashMap<StarSystem, Short> ssMapping = new HashMap<StarSystem, Short>();

		for (StarSystem starSystem : starSystems) {
			vertexBuffer.put(starSystem.getX());
			vertexBuffer.put(starSystem.getY());
			vertexBuffer.put(starSystem.getZ());

			int color = starSystem.getColor();
			float a = Color.alpha(color) / 255.0f;
			float r = Color.red(color) / 255.0f;
			float g = Color.green(color) / 255.0f;
			float b = Color.blue(color) / 255.0f;

			colorBuffer.put(r);
			colorBuffer.put(g);
			colorBuffer.put(b);
			colorBuffer.put(a);

			starsIndicesBuffer.put(index);
			ssMapping.put(starSystem, index);
			index++;
		}

		ArrayList<Pair<StarSystem, StarSystem>> links = engine.getUniverse().getLinks();
		linksVerticesCount = links.size() * 2;
		ByteBuffer nativeLinksIndicesBuffer = ByteBuffer.allocateDirect(linksVerticesCount * (Short.SIZE / Byte.SIZE));
		nativeLinksIndicesBuffer.order(ByteOrder.nativeOrder());
		linksIndicesBuffer = nativeLinksIndicesBuffer.asShortBuffer();

		for (Pair<StarSystem, StarSystem> link : links) {
			index = ssMapping.get(link.first);
			linksIndicesBuffer.put(index);
			index = ssMapping.get(link.second);
			linksIndicesBuffer.put(index);
		}

		vertexBuffer.position(0);
		colorBuffer.position(0);
		starsIndicesBuffer.position(0);
		linksIndicesBuffer.position(0);
	}

	@Override protected void draw(GL10 gl) {
		gl.glColor4f(1, 1, 1, 1);

		gl.glPointSize(3.0f);
		gl.glEnable(GL10.GL_POINT_SMOOTH);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
		// gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
		// gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		// gl.glNormalPointer(GL10.GL_FLOAT, 0, normalBuffer);
		// gl.glTexCoordPointer(X2COORDS, GL10.GL_FLOAT, 0, texcoordBuffer);
		gl.glVertexPointer(X3COORDS, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glColorPointer(RGBA, GL10.GL_FLOAT, 0, colorBuffer);

		gl.glDrawElements(GL10.GL_POINTS, starVerticesCount, GL10.GL_UNSIGNED_SHORT, starsIndicesBuffer);

		gl.glDrawElements(GL10.GL_LINES, linksVerticesCount, GL10.GL_UNSIGNED_SHORT, linksIndicesBuffer);
	}

	@Override public void onResume() {
		if (needPrepare) {
			prepareBuffers(engine);
			needPrepare = false;
		}
	}

	@Override public void onPause() {
		needPrepare = true;
	}
}
