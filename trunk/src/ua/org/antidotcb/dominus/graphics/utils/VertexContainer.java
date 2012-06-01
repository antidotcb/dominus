
package ua.org.antidotcb.dominus.graphics.utils;


import java.nio.FloatBuffer;


public interface VertexContainer {

	public int getNumVertices();

	public int getNumMaxVertices();

	public VertexFormat getAttributes();

	public void setVertices(float[] vertices, int offset, int count);

	public FloatBuffer getBuffer();

	public void bind();

	public void unbind();

	public void dispose();
}
