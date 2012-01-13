LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
 
LOCAL_MODULE    := hell
LOCAL_SRC_FILES := test.c
LOCAL_CFLAGS := -DANDROID_NDK
LOCAL_LDLIBS := -llog
 
include $(BUILD_SHARED_LIBRARY)