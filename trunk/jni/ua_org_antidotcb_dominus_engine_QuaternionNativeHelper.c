#include "ua_org_antidotcb_dominus_engine_QuaternionNativeHelper.h"
#include <string.h>
#include <android/log.h>

#define LOG_TAG "NDK DOMINUS"

JNIEXPORT jint Java_ua_org_antidotcb_dominus_engine_QuaternionNativeHelper_normalizeQuaternion(JNIEnv *env, jclass class, jobject obj) {
	__android_log_print(ANDROID_LOG_INFO, LOG_TAG, "This is message from NDK library");

	jclass quatClass = (*env).FindClass(env, "LJava_ua_org_antidotcb_dominus_engine_QuaternionNativeHelper_Quaternion");

	jfieldID xid = env->GetFieldID(env, obj, "x", "F");
	if (xid == 0) {
		__android_log_print(ANDROID_LOG_INFO, LOG_TAG, "Not found 'x' field");
	}

	jfieldID yid = (*env).GetFieldID(env, quatClass, "y", "F");
	if (yid == 0) {
		__android_log_print(ANDROID_LOG_INFO, LOG_TAG, "Not found 'y' field");
	}

	jfieldID zid = env->GetFieldID(env, class, "z", "F");
	if (xid == 0) {
		__android_log_print(ANDROID_LOG_INFO, LOG_TAG, "Not found 'z' field");
	}

	jfieldID wid = env->GetFieldID(env, class, "w", "F");
	if (xid == 0) {
		__android_log_print(ANDROID_LOG_INFO, LOG_TAG, "Not found 'w' field");
	}
}
