package ua.org.antidotcb.dominus.engine;

import android.util.Log;

public class QuaternionNativeHelper {
	public class Vector {
		public float	x, y, z;
	}

	public class Quaternion extends Vector {
		public float	w;
	}

	public class Matrix {
		public float	m[][]	= new float[4][4];
	}

	public QuaternionNativeHelper() {}

	private static native int normalizeQuaternion(Quaternion quat);

	static {
		try {

			System.loadLibrary("quaternion");
		} catch (UnsatisfiedLinkError e) {
			Log.d("ERROR", "UnsatisfiedLinkError", e);
			System.exit(-1);
		} catch (Exception e) {
			Log.d("ERROR", "Exception", e);
			System.exit(-1);
		}
	}
}
