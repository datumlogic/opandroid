LOCAL_PATH:= $(call my-dir)
#TOP_DIR := /Users/appl/Repos/github/NEW
include $(CLEAR_VARS)

SOURCE_PATH := $(LOCAL_PATH)/opandroid/openpeer-android-sdk/jni
INCLUDE_PATH := $(LOCAL_PATH)/../../libs/op/libs/op-core-cpp \
				$(LOCAL_PATH)/../../libs/op/libs/op-stack-cpp \
				$(LOCAL_PATH)/../../libs/op/libs/ortc-lib/libs/op-services-cpp \
				$(LOCAL_PATH)/../../libs/op/libs/ortc-lib/libs/zsLib \
				$(LOCAL_PATH)/../../libs/op/libs/ortc-lib/libs/op-services-cpp
LOCAL_EXPORT_C_INCLUDES := $(LOCAL_PATH)/../../libs/op/libs/op-core-cpp
include $(CLEAR_VARS)

LOCAL_MODULE := hfcore
LOCAL_SRC_FILES := libhfcore.a
LOCAL_EXPORT_C_INCLUDES := $(LOCAL_PATH)/../../libs/op/libs/op-core-cpp \
							$(LOCAL_PATH)/../../libs/op/libs/op-stack-cpp \
							$(LOCAL_PATH)/../../libs/op/libs/ortc-lib/libs/zsLib \
							$(LOCAL_PATH)/../../libs/op/libs/ortc-lib/libs/boost
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := hfservices
LOCAL_SRC_FILES := libhfservices.a
LOCAL_EXPORT_C_INCLUDES := $(LOCAL_PATH)/../../libs/op/libs/ortc-lib/libs/op-services-cpp
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := hfstack
LOCAL_SRC_FILES := libhfstack.a
LOCAL_EXPORT_C_INCLUDES := $(LOCAL_PATH)/../../libs/op/libs/op-stack-cpp
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := zslib
LOCAL_SRC_FILES := libzslib.a
LOCAL_EXPORT_C_INCLUDES := $(LOCAL_PATH)/../../../opios/libs/op/libs/zslib
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := udns
LOCAL_SRC_FILES := libudns.a
LOCAL_EXPORT_C_INCLUDES := $(LOCAL_PATH)/../../../opios/libs/op/libs
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := cryptopp
LOCAL_SRC_FILES := libcryptopp.a
LOCAL_EXPORT_C_INCLUDES := $(LOCAL_PATH)/../../../opios/libs/op/libs/cryptopp
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := curl
LOCAL_SRC_FILES := libcurl/lib/$(TARGET_ARCH_ABI)/libcurl.a
LOCAL_EXPORT_C_INCLUDES := libcurl/inc
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := idn
LOCAL_SRC_FILES := libidn.a
#LOCAL_EXPORT_C_INCLUDES := libcurl/inc
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := z
LOCAL_SRC_FILES := libz.a
#LOCAL_EXPORT_C_INCLUDES := libcurl/inc
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := boost_date_time
LOCAL_SRC_FILES := libboost_date_time-gcc-mt-1_49.a
LOCAL_EXPORT_C_INCLUDES := $(LOCAL_PATH)/../../../opios/libs/op/boost/boost
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := boost_regex
LOCAL_SRC_FILES := libboost_regex-gcc-mt-1_49.a
LOCAL_EXPORT_C_INCLUDES := $(LOCAL_PATH)/../../../opios/libs/op/boost/boost
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := boost_thread
LOCAL_SRC_FILES := libboost_thread-gcc-mt-1_49.a
LOCAL_EXPORT_C_INCLUDES := $(LOCAL_PATH)/../../../opios/libs/op/boost/boost
include $(PREBUILT_STATIC_LIBRARY)

#include $(CLEAR_VARS)
#LOCAL_MODULE := stlport_shared
#LOCAL_SRC_FILES := libstlport_shared.so
#LOCAL_EXPORT_C_INCLUDES := $(LOCAL_PATH)/../../../opios/libs/op/boost/boost
#include $(PREBUILT_SHARED_LIBRARY)

# main shared library
include $(CLEAR_VARS)

SOURCE_PATH := $(LOCAL_PATH)/opandroid/openpeer-android-sdk/jni

#LOCAL_CFLAGS := -D_ANDROID $(LOCAL_CFLAGS)
LOCAL_CFLAGS := \
    '-DWEBRTC_TARGET_PC' \
    '-DWEBRTC_ANDROID'
LOCAL_C_INCLUDES := $(LOCAL_PATH)/../../../opios/libs/op \
		    $(LOCAL_PATH)/../../../opios/libs/op/libs/webrtc \
		    $(LOCAL_PATH)/../../../opios/libs/op/libs/webrtc/webrtc \
		    $(LOCAL_PATH)/../../../opios/libs/op/libs/webrtc/webrtc/voice_engine/include \
		    $(LOCAL_PATH)/../../../opios/libs/op/libs/webrtc/webrtc/video_engine/include \
		    $(LOCAL_PATH)/../../../opios/libs/op/libs/webrtc/webrtc/modules/video_capture/include \
		    $(LOCAL_PATH)/../../../opios/libs/op/libs/zsLib \
		    $(LOCAL_PATH)/../../../opios/libs/op/libs \
		   $(LOCAL_PATH)/libcurl/include \
		    $(LOCAL_PATH)/../../../opios/libs/op/libs/boost/boost

LOCAL_MODULE := opjni
LOCAL_SRC_FILES := globals.cpp \
		   EventManager.cpp \
		   com_openpeer_javaapi_OPStackMessageQueue.cpp \
		   com_openpeer_javaapi_OPStack.cpp \
		   com_openpeer_javaapi_OPAccount.cpp \
		   com_openpeer_javaapi_OPIdentity.cpp
		   
MY_STATIC_LIBRARIES := hfcore hfstack hfservices udns boost_date_time boost_regex boost_thread idn z curl cryptopp zslib
#LOCAL_SHARED_LIBRARIES := stlport_shared
LOCAL_WHOLE_STATIC_LIBRARIES := $(MY_STATIC_LIBRARIES)

LOCAL_EXPORT_C_INCLUDES := $(LOCAL_PATH)/../../../opios/libs/op \
		    $(LOCAL_PATH)/../../../opios/libs/op/libs/webrtc \
		    $(LOCAL_PATH)/../../../opios/libs/op/libs/webrtc/webrtc \
		    $(LOCAL_PATH)/../../../opios/libs/op/libs/webrtc/webrtc/voice_engine/include \
		    $(LOCAL_PATH)/../../../opios/libs/op/libs/webrtc/webrtc/video_engine/include \
		    $(LOCAL_PATH)/../../../opios/libs/op/libs/webrtc/webrtc/modules/video_capture/include \
		    $(LOCAL_PATH)/../../../opios/libs/op/libs/zsLib \
		    $(LOCAL_PATH)/../../../opios/libs/op/libs \
		    $(LOCAL_PATH)/../../../opios/libs/op/libs/zslib \
		    $(LOCAL_PATH)/../../../opios/libs/op/libs/boost/boost
#LD_LIBRARY_PATH += $(LOCAL_PATH)/opandroid/openpeer-android-sdk/jni 

MY_STATIC_LIBRARIES000 := libz.a
MY_STATIC_LIBRARIES001 := libstlport_static.a
MY_STATIC_LIBRARIES00 := libgcrypt.a
MY_STATIC_LIBRARIES01 := libssh2.a
MY_STATIC_LIBRARIES02 := libssl.a
MY_STATIC_LIBRARIES0 := libidn.a
MY_STATIC_LIBRARIES1 := libcurl.a
MY_STATIC_LIBRARIES2 := libboost_date_time-gcc-mt-1_49.a
MY_STATIC_LIBRARIES3 := libboost_regex-gcc-mt-1_49.a
MY_STATIC_LIBRARIES4 := libboost_thread-gcc-mt-1_49.a
MY_STATIC_LIBRARIES5 := libcryptopp.a
MY_STATIC_LIBRARIES6 := libudns.a
MY_STATIC_LIBRARIES7 := libzslib.a
MY_STATIC_LIBRARIES8 := libhfservices.a
MY_STATIC_LIBRARIES9 := libhfstack.a
MY_STATIC_LIBRARIES10 := libhfcore.a

LOCAL_LDLIBS := \
    -llog \
    -lgcc

LOCAL_LDLIBS += $(LOCAL_PATH)/$(MY_STATIC_LIBRARIES000)
#LOCAL_LDLIBS += $(LOCAL_PATH)/$(MY_STATIC_LIBRARIES001)
#LOCAL_LDLIBS += $(LOCAL_PATH)/$(MY_STATIC_LIBRARIES00)
#LOCAL_LDLIBS += $(LOCAL_PATH)/$(MY_STATIC_LIBRARIES01)
#LOCAL_LDLIBS += $(LOCAL_PATH)/$(MY_STATIC_LIBRARIES02)
LOCAL_LDLIBS += $(LOCAL_PATH)/$(MY_STATIC_LIBRARIES0)
LOCAL_LDLIBS += $(LOCAL_PATH)/libcurl/lib/$(TARGET_ARCH_ABI)/$(MY_STATIC_LIBRARIES1)
LOCAL_LDLIBS += $(LOCAL_PATH)/$(MY_STATIC_LIBRARIES2)
LOCAL_LDLIBS += $(LOCAL_PATH)/$(MY_STATIC_LIBRARIES3)
LOCAL_LDLIBS += $(LOCAL_PATH)/$(MY_STATIC_LIBRARIES4)
LOCAL_LDLIBS += $(LOCAL_PATH)/$(MY_STATIC_LIBRARIES5)
LOCAL_LDLIBS += $(LOCAL_PATH)/$(MY_STATIC_LIBRARIES6)
LOCAL_LDLIBS += $(LOCAL_PATH)/$(MY_STATIC_LIBRARIES7)
LOCAL_LDLIBS += $(LOCAL_PATH)/$(MY_STATIC_LIBRARIES8)
LOCAL_LDLIBS += $(LOCAL_PATH)/$(MY_STATIC_LIBRARIES9)
LOCAL_LDLIBS += $(LOCAL_PATH)/$(MY_STATIC_LIBRARIES10)

#LOCAL_LDFLAGS += -L$(LD_LIBRARY_PATH) -ludns
LOCAL_PRELINK_MODULE := false
#LOCAL_ALLOW_UNDEFINED_SYMBOLS := true

include $(BUILD_SHARED_LIBRARY)

#$(call import-add-path,$(LOCAL_PATH))
#$(call import-module,curl)

