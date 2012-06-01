
package ua.org.antidotcb.dominus.graphics;


import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.util.Log;


public class Model
	extends CompositeMesh {

	FloatBuffer	coordsBuffer;
	FloatBuffer	normalsBuffer;
	FloatBuffer	texcoordsBuffer;

	public Model(String name) {
		// TODO Auto-generated constructor stub
	}

	protected static int loadTexture(GL10 gl, Bitmap bmp) {
		ByteBuffer bb = ByteBuffer.allocateDirect(bmp.getHeight() * bmp.getWidth() * 4);
		bb.order(ByteOrder.nativeOrder());
		IntBuffer ib = bb.asIntBuffer();

		for (int y = 0; y < bmp.getHeight(); y++)
			for (int x = 0; x < bmp.getWidth(); x++) {
				ib.put(bmp.getPixel(x, y));
			}
		ib.position(0);
		bb.position(0);

		int[] tmp_tex = new int[1];

		gl.glGenTextures(1, tmp_tex, 0);
		int tx = tmp_tex[0];
		gl.glBindTexture(GL10.GL_TEXTURE_2D, tx);
		gl.glTexImage2D(GL10.GL_TEXTURE_2D, 0, GL10.GL_RGBA, bmp.getWidth(), bmp.getHeight(), 0, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, bb);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
		return tx;
	}

	//int	i	= 0;

	@Override public void render(GL10 gl) {
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
		gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, coordsBuffer);

		gl.glNormalPointer(GL10.GL_FLOAT, 0, normalsBuffer);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texcoordsBuffer);
		gl.glColorPointer(3, GL10.GL_FLOAT, 0, coordsBuffer);

		gl.glEnable(GL10.GL_TEXTURE_2D);
//		switch (i) {
//		case 0:
//			i = 3;
//			break;
//		case 3:
//			i = 6;
//			break;
//		case 6:
//			i = 8;
//			break;
//		case 8:
//			i = 0;
//			break;
//		}

		super.render(gl);

		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
		gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
	}

	public void setVertices(float[] vertices) {
		ByteBuffer nativeVerticesBuffer = ByteBuffer.allocateDirect(3 * (vertices.length / 8) * (Float.SIZE / Byte.SIZE));
		nativeVerticesBuffer.order(ByteOrder.nativeOrder());
		coordsBuffer = nativeVerticesBuffer.asFloatBuffer();

		ByteBuffer nativeNormalsBuffer = ByteBuffer.allocateDirect(3 * (vertices.length / 8) * (Float.SIZE / Byte.SIZE));
		nativeNormalsBuffer.order(ByteOrder.nativeOrder());
		normalsBuffer = nativeNormalsBuffer.asFloatBuffer();

		ByteBuffer nativeTexCoordsBuffer = ByteBuffer.allocateDirect(2 * (vertices.length / 8) * (Float.SIZE / Byte.SIZE));
		nativeTexCoordsBuffer.order(ByteOrder.nativeOrder());
		texcoordsBuffer = nativeTexCoordsBuffer.asFloatBuffer();

		Log.i(TAG, "REPARSE");
		for (int i = 0; i < vertices.length; i++) {
			int j = i / 8;
			int n = i - j * 8;
			if (n < 3) {
				coordsBuffer.put(vertices[i]);
				Log.i(TAG, "COORD:" + vertices[i]);
			} else
				if (n < 6) {
					normalsBuffer.put(vertices[i]);
					Log.i(TAG, "NORMAL:" + vertices[i]);
				} else {
					texcoordsBuffer.put(vertices[i]);
					Log.i(TAG, "TEXCOORD:" + vertices[i]);
				}
		}
		coordsBuffer.position(0);
		normalsBuffer.position(0);
		texcoordsBuffer.position(0);
	}

	private final static String	TAG	= ObjParser.class.getName();

	public void setVertexFormat() {

	}
}
