LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := dominus
### Add all source file names to be included in lib separated by a whitespace
LOCAL_SRC_FILES := dominus.cpp

include $(BUILD_SHARED_LIBRARY)
