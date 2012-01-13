#include <string.h>
#include <jni.h>
#include <android/log.h>

JNIEXPORT jstring Java_ua_org_antidotcb_dominus_engine_NativeHelper_nativeFunction(
		JNIEnv* env, jobject javaThis) {
	__android_log_print(ANDROID_LOG_INFO, "Hello NDK!",
			"This is message from NDK library");
	int i = 2;
	int b = i;
	int c = b + i;
	return (*env)->NewStringUTF(env, "Hello from native code!");
}
