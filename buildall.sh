#!/bin/sh
if [ -z $ANDROID_NDK_HOME ];then
   echo "Please set $ANDROID_NDK_HOME to you ndk path"
   exit -1;
fi
if [ -z $ANDROID_HOME ];then
   echo "Please set $ANDROID_HOME to you sdk path"
   exit -2;
fi
./buildall_android.sh $ANDROID_NDK_HOME
pushd `pwd`
echo "start building sdk"
cd openpeer_sdk_android
ant release
popd
pushd `pwd`
echo "start building sample app"
cd OpenPeerSampleApp
ant release
popd
