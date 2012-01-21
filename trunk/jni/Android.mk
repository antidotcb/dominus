LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
 
LOCAL_MODULE    := quaternion
LOCAL_CPP_EXTENSION = cpp
LOCAL_SRC_FILES := quaternion.cpp
LOCAL_CFLAGS := -DANDROID_NDK -Wall -g
LOCAL_LDLIBS := -llog
 
include $(BUILD_SHARED_LIBRARY)