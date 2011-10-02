package ua.org.antidotcb.dominus;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;
import ua.org.antidotcb.dominus.engine.Engine;
import ua.org.antidotcb.dominus.engine.StarSystem;
import android.content.Context;
import android.graphics.Color;
import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;
import android.util.Pair;

public class DefaultUniverseRenderer extends AbstractRenderer implements
		Renderer {

	private final static int COORDS = 3;
	private final static int COLORS = 4;
	private int starVerticesCount;
	private int linksVerticesCount;

	public DefaultUniverseRenderer(Context context, Engine engine) {
		ArrayList<StarSystem> starSystems = engine.getUniverse()
				.getStarSystems();
		starVerticesCount = starSystems.size();

		ByteBuffer nativeVertexBuffer = ByteBuffer.allocateDirect((starVerticesCount)
				* (Float.SIZE / Byte.SIZE) * COORDS);
		nativeVertexBuffer.order(ByteOrder.nativeOrder());
		vertexBuffer = nativeVertexBuffer.asFloatBuffer();

		ByteBuffer nativeColorBuffer = ByteBuffer.allocateDirect((starVerticesCount)
				* (Float.SIZE / Byte.SIZE) * COLORS);
		nativeColorBuffer.order(ByteOrder.nativeOrder());
		colorBuffer = nativeColorBuffer.asFloatBuffer();

		ByteBuffer nativeStarIndicesBuffer = ByteBuffer.allocateDirect((starVerticesCount)
				* (Short.SIZE / Byte.SIZE));
		nativeStarIndicesBuffer.order(ByteOrder.nativeOrder());
		starsIndicesBuffer = nativeStarIndicesBuffer.asShortBuffer();
		
		short index = 0;

		for (StarSystem starSystem : starSystems) {
			vertexBuffer.put(starSystem.getX());
			vertexBuffer.put(starSystem.getY());
			vertexBuffer.put(starSystem.getZ());

			int color = starSystem.getColor();
			float a = (float) Color.alpha(color) / 255.0f;
			float r = (float) Color.red(color) / 255.0f;
			float g = (float) Color.green(color) / 255.0f;
			float b = (float) Color.blue(color) / 255.0f;

			colorBuffer.put(r);
			colorBuffer.put(g);
			colorBuffer.put(b);
			colorBuffer.put(a);

			starsIndicesBuffer.put(index);
			index++;
		}
		
		ArrayList<Pair<StarSystem, StarSystem>> links = engine.getUniverse().getLinks();
		linksVerticesCount = links.size() * 2;
		ByteBuffer nativeLinksIndicesBuffer = ByteBuffer.allocateDirect(linksVerticesCount
				* (Short.SIZE / Byte.SIZE));
		nativeLinksIndicesBuffer.order(ByteOrder.nativeOrder());
		linksIndicesBuffer = nativeLinksIndicesBuffer.asShortBuffer();
		
		for (Pair<StarSystem, StarSystem> link : links )
		{
			if(link.first == null)
				Log.wtf(Engine.eTag, "WTF. NULL START");
			linksIndicesBuffer.put(link.first.getId());
			if(link.second == null)
				Log.wtf(Engine.eTag, "WTF. NULL START");
			linksIndicesBuffer.put(link.second.getId());
		}
		
		vertexBuffer.position(0);
		colorBuffer.position(0);
		starsIndicesBuffer.position(0);
		linksIndicesBuffer.position(0);
	}

	@Override
	protected void draw(GL10 gl) {
		gl.glColor4f(1, 1, 1, 1);
		// gl.glFrontFace(GL10.GL_CCW);
		gl.glPointSize(3.0f);
		gl.glEnable(GL10.GL_POINT_SMOOTH);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
		gl.glVertexPointer(COORDS, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glColorPointer(COLORS, GL10.GL_FLOAT, 0, colorBuffer);
		// gl.glEnable(GL10.GL_TEXTURE_2D);
		// gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTexBuffer);
		gl.glDrawElements(GL10.GL_POINTS, starVerticesCount, GL10.GL_UNSIGNED_SHORT,
				starsIndicesBuffer);

		gl.glDrawElements(GL10.GL_LINES, linksVerticesCount, GL10.GL_UNSIGNED_SHORT,
				linksIndicesBuffer);
	}

	private ShortBuffer starsIndicesBuffer;
	private ShortBuffer linksIndicesBuffer;

	private FloatBuffer vertexBuffer;

	private FloatBuffer colorBuffer;

}
