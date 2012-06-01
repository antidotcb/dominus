
package ua.org.antidotcb.dominus.graphics;


import java.util.Vector;

import javax.microedition.khronos.opengles.GL10;


public abstract class CompositeMesh
	implements ComponentMesh {

	private Vector<ComponentMesh>	content	= new Vector<ComponentMesh>();

	public void add(ComponentMesh component) {
		content.add(component);
	}

	public ComponentMesh get(int location) {
		return content.get(location);
	}

	public void remove(ComponentMesh component) {
		content.remove(component);
	}

	public void render(GL10 gl) {
		for (ComponentMesh mesh : content) {
			mesh.render(gl);
		}
	}

	public int size() {
		return content.size();
	}

}
