package ua.org.antidotcb.dominus;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.opengl.GLSurfaceView.Renderer;

public abstract class AbstractRenderer implements Renderer {

	protected abstract void draw(GL10 gl);

	public void onDrawFrame(GL10 gl) {
        /*
         * By default, OpenGL enables features that improve quality
         * but reduce performance. One might want to tweak that
         * especially on software renderer.
         */
        gl.glDisable(GL10.GL_DITHER);

        /*
         * Usually, the first thing one might want to do is to clear
         * the screen. The most efficient way of doing this is to use
         * glClear().
         */

        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
        gl.glTranslatef(0, 0, -2.0f);
        gl.glRotatef(angleX, 0, 1, 0);
        gl.glRotatef(angleY, 1, 0, 0);

        //GLU.gluLookAt(gl, 0, 0, -2, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        //gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

		draw(gl);
	}

	public void onSurfaceChanged(GL10 gl, int width, int height) {
		gl.glViewport(0, 0, width, height);
		float ratio = (float) width / height;
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glFrustumf(-ratio, ratio, -1.0f, 1.0f, 1.0f, 3.0f);
	}

	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        /*
         * By default, OpenGL enables features that improve quality
         * but reduce performance. One might want to tweak that
         * especially on software renderer.
         */
        gl.glDisable(GL10.GL_DITHER);
        gl.glEnable(GL10.GL_BLEND);

        /*
         * Some one-time OpenGL initialization can be made here
         * probably based on features of this particular context
         */
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT,
                GL10.GL_FASTEST);
        
        gl.glHint(GL10.GL_POINT_SMOOTH_HINT, GL10.GL_NICEST);

        gl.glClearColor(0, 0, 0, 1);
        gl.glShadeModel(GL10.GL_SMOOTH);
        gl.glEnable(GL10.GL_DEPTH_TEST);
	}

	public void setAngleX(float angleX) {
		this.angleX = angleX;
	}

	public float getAngleX() {
		return angleX;
	}

	public void setAngleY(float angleY) {
		this.angleY = angleY;
	}

	public float getAngleY() {
		return angleY;
	}

	private float angleX;
	private float angleY;
}
