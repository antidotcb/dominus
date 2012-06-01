
package ua.org.antidotcb.dominus.graphics;


import javax.microedition.khronos.opengles.GL10;


public interface ComponentMesh {

	void add(ComponentMesh component);

	ComponentMesh get(int location);

	void remove(ComponentMesh component);

	void render(GL10 gl);

	int size();
}
