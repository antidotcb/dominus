
package ua.org.antidotcb.dominus.graphics;


import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;


public class Mesh
	implements ComponentMesh {

	private ShortBuffer	indicesBuffer;

	public Mesh(String name) {
		// TODO Auto-generated constructor stub
	}

	public void add(ComponentMesh component) {
		throw new UnsupportedOperationException("Not implemented for" + this.getClass().getName());
	}

	public ComponentMesh get(int index) {
		return null;
	}

	public void remove(ComponentMesh component) {
		throw new UnsupportedOperationException("Not implemented for" + this.getClass().getName());
	}

	public void render(GL10 gl) {
		gl.glScalef(1.1f, 1.1f, 1.1f);
		gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
		gl.glLineWidth(3.0f);
		gl.glColor4f(0, 0, 0, 1);
		gl.glDrawElements(GL10.GL_LINE_STRIP, indicesBuffer.remaining(), GL10.GL_UNSIGNED_SHORT, indicesBuffer);
		gl.glScalef(0.909090909f, 0.909090909f, 0.909090909f);
		
		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
		gl.glColor4f(1, 1, 1, 1);
		gl.glDrawElements(GL10.GL_TRIANGLES, indicesBuffer.remaining(), GL10.GL_UNSIGNED_SHORT, indicesBuffer);
	}

	public int size() {
		return 0;
	}

	public void setIndices(short[] indices) {
		ByteBuffer nativeIndicesBuffer = ByteBuffer.allocateDirect(indices.length * (Short.SIZE / Byte.SIZE));
		nativeIndicesBuffer.order(ByteOrder.nativeOrder());
		indicesBuffer = nativeIndicesBuffer.asShortBuffer();
		indicesBuffer.put(indices);
		indicesBuffer.position(0);
	}
}
