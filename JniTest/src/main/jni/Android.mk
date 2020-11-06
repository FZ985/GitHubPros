LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := wy_sdk-lib
LOCAL_SRC_FILES =: wy_sdk-lib.cpp
LOCAL_LDLIBS += -llog
include $(BUILD_SHARED_LIBRARY)