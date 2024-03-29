#include "quaternion.h"
#include <string.h>
#include <android/log.h>
#include <math.h>

#define LOG_TAG "NDK DOMINUS"

JNIEXPORT jint JNICALL Java_ua_org_antidotcb_dominus_engine_NativeQuatUtils_normalizeQuaternion(JNIEnv* env, jclass clazz, jobject obj) {
	__android_log_print(ANDROID_LOG_INFO, LOG_TAG, "This is message from NDK library");

	jclass quaternionClass = env->GetObjectClass(obj);

	jfieldID xid = env->GetFieldID(quaternionClass, "x", "F");
	jfieldID yid = env->GetFieldID(quaternionClass, "y", "F");
	jfieldID zid = env->GetFieldID(quaternionClass, "z", "F");
	jfieldID wid = env->GetFieldID(quaternionClass, "w", "F");

	jfloat x = env->GetFloatField(obj, xid);
	jfloat y = env->GetFloatField(obj, yid);
	jfloat z = env->GetFloatField(obj, zid);
	jfloat w = env->GetFloatField(obj, wid);

	return x*x + y*y + z*z + w * w;
}
