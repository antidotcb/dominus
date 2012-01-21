package ua.org.antidotcb.dominus.engine;

public class NativeQuatUtils {

	private NativeQuatUtils() {}

	public static native int normalizeQuaternion(Quaternion quat);
}
