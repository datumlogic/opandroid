#!/bin/sh

#Note [TBD] : There is no check for ndk-version
#Please use the ndk-version as per host machine for now

#Get the machine type
PROCTYPE=`uname -m`

if [ "$PROCTYPE" = "i686" ] || [ "$PROCTYPE" = "i386" ] || [ "$PROCTYPE" = "i586" ] ; then
        echo "Host machine : x86"
        ARCHTYPE="x86"
else
        echo "Host machine : x86_64"
        ARCHTYPE="x86_64"
fi

#Get the Host OS
HOST_OS=`uname -s`
case "$HOST_OS" in
    Darwin)
        HOST_OS=darwin
        ;;
    Linux)
        HOST_OS=linux
        ;;
esac

#ndk-path
if [[ $1 == *android-ndk-* ]]; then
	echo "----------------- NDK Path is : $1 ----------------"
	Input1=$1;
else
	echo "Please enter your android ndk path:"
	echo "For example:/home/astro/android-ndk-r8e"
	read Input1
	echo "You entered:$Input1"
fi

#sdk-path
if [[ $2 == *android-sdk* ]]; then
	echo "----------------- SDK Path is : $2 ----------------"
	Input2=$2;
elif [[ -n "$ANDROID_HOME" ]]; then
    Input2=$ANDROID_HOME;
	echo "----------------- SDK Path is : $ANDROID_HOME ----------------"
else
	echo "Please enter your android sdk path:"
	echo "For example:/home/astro/android-sdk"
	read Input2
	echo "You entered:$Input2"
fi

#Set path
echo "----------------- Exporting the android-ndk path ----------------"
export PATH=$PATH:$Input1:$Input1/toolchains/arm-linux-androideabi-4.7/prebuilt/$HOST_OS-$ARCHTYPE/bin

#build all modules
echo "******************BUILD STARTED FOR ALL ORTC LIBS*******************"

#boost build -- 1
pushd `pwd`
cd libs/op/libs/ortc-lib/libs/boost/projects
./build_boost_android.sh $Input1
popd

#openssl build -- 2
pushd `pwd`
cd libs/op/libs/ortc-lib/libs/openssl/projects/android
./build-libssl_android.sh $Input1
popd

#curl build -- 3
pushd `pwd`
cd libs/op/libs/ortc-lib/libs/curl/projects/android
./build_curl_android.sh $Input1
popd

#cryptopp build -- 4
pushd `pwd`
cd libs/op/libs/ortc-lib/libs/cryptopp/projects/android-patches
./build_cryptopp_android.sh $Input1
popd

#udns build -- 5
pushd `pwd`
cd libs/op/libs/ortc-lib/libs/udns/projects/android
./build_udns_android.sh $Input1
popd

#zsLib build -- 6
pushd `pwd`
cd libs/op/libs/ortc-lib/libs/zsLib/projects/android
./build_zsLib_android.sh $Input1
popd

#op-services-cpp build -- 7
pushd `pwd`
cd libs/op/libs/ortc-lib/libs/op-services-cpp/projects/android
./build_op-services-cpp_android.sh $Input1
popd

#webrtc build -- 8
pushd `pwd`
cd libs/op/libs/ortc-lib/libs/webrtc/projects/android
./build_webrtc_android.sh $Input1 $Input2
popd

#op-stack-cpp build -- 9
pushd `pwd`
cd libs/op/libs/op-stack-cpp/projects/android
./build_op-stack-cpp_android.sh $Input1
popd

#op-core-cpp build - 10
pushd `pwd`
cd libs/op/libs/op-core-cpp/projects/android
./build_op-core-cpp_android.sh $Input1
popd

#openpeer-android-sdk shared build - 11
pushd `pwd`
cd openpeer-android-sdk
./build_openpeer_android_sdk_android.sh $Input1
popd

echo "******************BUILD COMPLETED FOR ALL ANDROID SDK LIBS*******************"



