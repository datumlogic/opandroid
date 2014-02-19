NDK_TOOLCHAIN_VERSION=4.7
APP_PROJECT_PATH := $(shell pwd)
APP_BUILD_SCRIPT := $(APP_PROJECT_PATH)/jni/Android.mk
APP_ABI := armeabi armeabi-v7a
APP_STL := gnustl_static
APP_CPPFLAGS := -frtti -fexceptions -D__GLIBC__ -D__GLIBCPP__ -D_STLP_USE_SIMPLE_NODE_ALLOC
APP_MODULES := openpeer

