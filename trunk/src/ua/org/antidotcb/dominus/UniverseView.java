
package ua.org.antidotcb.dominus;


import ua.org.antidotcb.dominus.graphics.AbstractRenderer;
import ua.org.antidotcb.dominus.graphics.ObjRenderer;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;


public class UniverseView
	extends GLSurfaceView {

	private boolean				debugFlag				= false;

	private float				previousX;
	private float				previousY;
	private AbstractRenderer	renderer;
	private final float			TOUCH_SCALE_FACTOR		= 180.0f / 320;

	private final float			TRACKBALL_SCALE_FACTOR	= 36.0f;

	public UniverseView(Context context) {
		super(context);
		initialize(context);
	}

	public UniverseView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize(context);
	}

	protected void initialize(Context context) {
		setEGLConfigChooser(false);
		if (!isInEditMode()) {
			if (debugFlag) setDebugFlags(GLSurfaceView.DEBUG_LOG_GL_CALLS | GLSurfaceView.DEBUG_CHECK_GL_ERROR);

			// renderer = new DefaultUniverseRenderer(context,
			// Engine.Instance());
			renderer = new ObjRenderer(context, "quit2.obj");
			setRenderer(renderer);
			setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
		}
	}

	/*
	 * public void onPause() { super.onPause(); renderer.onPause(); }
	 * public void onResume() { super.onResume(); renderer.onResume(); }
	 */

	@Override public boolean onTouchEvent(MotionEvent e) {
		if (!isInEditMode()) {
			float x = e.getX();
			float y = e.getY();
			switch (e.getAction()) {
			case MotionEvent.ACTION_MOVE:
				float dx = x - previousX;
				float dy = y - previousY;
				renderer.setAngleX(renderer.getAngleX() + dx * TOUCH_SCALE_FACTOR);
				renderer.setAngleY(renderer.getAngleY() + dy * TOUCH_SCALE_FACTOR);
				requestRender();
				break;
			}
			previousX = x;
			previousY = y;
			return true;
		}

		return false;
	}

	@Override public boolean onTrackballEvent(MotionEvent event) {
		if (!isInEditMode()) {
			renderer.setAngleX(renderer.getAngleX() + event.getX() * TRACKBALL_SCALE_FACTOR);
			renderer.setAngleY(renderer.getAngleY() + event.getY() * TRACKBALL_SCALE_FACTOR);
			requestRender();
			return true;
		}

		return false;
	}

}
