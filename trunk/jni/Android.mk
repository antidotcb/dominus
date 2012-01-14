LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
 
LOCAL_MODULE    := quaternion
LOCAL_SRC_FILES := ua_org_antidotcb_dominus_engine_QuaternionNativeHelper.c
LOCAL_CFLAGS := -DANDROID_NDK
LOCAL_LDLIBS := -llog
 
include $(BUILD_SHARED_LIBRARY)