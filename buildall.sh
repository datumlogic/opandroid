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
echo "start building sample app and sdk"
cd OpenPeerSampleApp
ant release
echo "Done building sample app and sdk"
popd
