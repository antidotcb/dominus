package ua.org.antidotcb.dominus.engine;

import android.util.Log;

public class NativeHelper {
	static {
		try {

			System.loadLibrary("hell");
		} catch (UnsatisfiedLinkError e) {
			Log.d("ERROR", "UnsatisfiedLinkError", e);
		} catch (Exception e) {
			Log.d("ERROR", "Exception", e);
		}
	}

	public native String nativeFunction();

	String	test;

	public NativeHelper() {
		test = nativeFunction();
		Log.i("Hello SDK!", String.format("This is message from SDK library\ntest_value=%s", test));
	}
}
