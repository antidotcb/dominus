
package ua.org.antidotcb.dominus.graphics;


import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView.Renderer;


public abstract class AbstractRenderer
	implements Renderer {

	private float	angleX;
	private float	angleY;

	protected abstract void draw(GL10 gl);

	public float getAngleX() {
		return angleX;
	}

	public float getAngleY() {
		return angleY;
	}

	public void onDrawFrame(GL10 gl) {
		gl.glDisable(GL10.GL_DITHER);

		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glTranslatef(0, 0, -2.0f);
		gl.glRotatef(angleX, 0, 1, 0);
		gl.glRotatef(angleY, 1, 0, 0);

		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		draw(gl);
	}

	public void onSurfaceChanged(GL10 gl, int width, int height) {
		gl.glViewport(0, 0, width, height);
		float ratio = (float) width / height;
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glFrustumf(-ratio, ratio, -1.0f, 1.0f, 1.0f, 3.0f);
	}

	float	lightAmbient[]	= new float[] { 0.2f, 0.3f, 0.6f, 1.0f };
	float	lightDiffuse[]	= new float[] { 1f, 1f, 1f, 1.0f };

	float	matAmbient[]	= new float[] { 1f, 1f, 1f, 1.0f };
	float	matDiffuse[]	= new float[] { 1f, 1f, 1f, 1.0f };

	float[]	pos				= new float[] { 0, 20, 20, 1 };

	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		gl.glDisable(GL10.GL_DITHER);
		gl.glEnable(GL10.GL_BLEND);

		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);

		// gl.glHint(GL10.GL_POINT_SMOOTH_HINT, GL10.GL_NICEST);

		gl.glClearColor(0, 0, 0, 1);
		gl.glShadeModel(GL10.GL_SMOOTH);
		gl.glEnable(GL10.GL_DEPTH_TEST);

		gl.glClearDepthf(1.0f);
		gl.glDepthFunc(GL10.GL_LEQUAL);

		// gl.glEnable(GL10.GL_LIGHTING);
		// gl.glEnable(GL10.GL_LIGHT0);
		// gl.glEnable(GL10.GL_NORMALIZE);

		// gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, lightAmbient, 0);
		// gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, lightDiffuse, 0);
		// gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, pos, 0);

		// gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT, matAmbient,
		// 0);
		// gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, matDiffuse,
		// 0);

		// Pretty perspective
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
		// gl.glEnable(GL10.GL_CULL_FACE);
		// gl.glCullFace(GL10.GL_CCW);
	}

	public void setAngleX(float angleX) {
		this.angleX = angleX;
	}

	public void setAngleY(float angleY) {
		this.angleY = angleY;
	}

	public void onResume() {}

	public void onPause() {}
}
