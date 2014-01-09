LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := OpenPeerSampleApp
LOCAL_SRC_FILES := OpenPeerSampleApp.cpp

include $(BUILD_SHARED_LIBRARY)
