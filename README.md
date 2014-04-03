Thank you for downloading Hookflash's Open Peer Android SDK.

For a quick introduction to the code please read the following. For more detailed instructions please go to http://docs.hookflash.com.


From your terminal, please clone the “opandroid” git repository:
git clone --recursive https://github.com/openpeer/opandroid.git

This repository will yield the Android Java SDK, Native sample application and dependency libraries like the C++ open peer core, stack, media and libraries needed to support the underlying SDK.

Directory structure:
opandroid/                            - contains the project files for building the Open Peer Android SDK
opandroid/openpeer-android-sdk/       - contains the Open Peer Android Java project files and source folders
opandroid/openpeer-android-sdk/jni/   - contains the Java SDK JNI wires
opandroid/openpeer-ios-sdk/src/       - contains the implementation of the Android SDK files
opandroid/OpenPeerNativeSampleApp/    - contains the sample application which implements some Android SDK functionality
opandroid/OpenPeerSdkCSharp/          - contains the CSharp wrapper of Java SDK (Xamarin ready)


How to build:

0) Prerequisites

a) Android ADT bundle installed
- Download and install from https://developer.android.com/sdk/index.html

b) Android NDK installed
- Download and install from https://developer.android.com/tools/sdk/ndk/index.html
NOTE: openpeer-android-sdk was developed and tested with android-ndk-r8e !!!

c) Setup Eclipse to point to proper NDK
- Go to ADT->Preferences->Android->NDK and set NDK path

1) Download and build required native 3rd party libraries by running buildall_android.sh script from your terminal:

pushd opandroid/
./buildall_android.sh
popd

NOTE: It is required to have Android NDK r8e installed on the target machine. Path to the NDK is mandatory input for building 3rd party libraries.


2) From Eclipse ADT, load sdk project:

opandroid/openpeer—android-sdk/.project (Android Java SDK project)

or sample project with included SDK project

opandroid/OpenPeerNativeSampleApp/.project (Native sample app project)

3) Build imported project using Eclipse with ADT plugin



Exploring the dependency libraries:
libs/op/libs/ortc-lib/libs/zsLib     	    - asynchronous communication library for C++
libs/op/libs/ortc-lib/libs/udns      	    - C language DNS resolution library
libs/op/libs/ortc-lib/libs/cryptopp   	    – C++ cryptography language
libs/op/libs/ortc-lib/libs/openssl	    - C++ Hookflash Open Peer communication services layer
libs/op/libs/ortc-lib/libs/op-services-cpp  - C++ Hookflash Open Peer communication services layer
libs/op/libs/op-stack-cpp    		    – C++ Hookflash Open Peer stack
libs/op/libs/op-core-cpp    		    – C++ Hookflash Open Peer core API (works on the Open Peer stack)
libs/op/libs/ortc-lib/libs/WebRTC           – android port of the webRTC media stack


Branches:

Our current activity is being performed on "201404041-dev-stable” but this branch is unstable. Individual activity is on other sub-branches from this branch.
https://github.com/openpeer/opandroid/tree/20140401-dev-stable

To see all branches go to:
https://github.com/openpeer/opandroid/branches


Contact info:

Please contact robin@hookflash.com if you have any suggestions to improve the API. Please use support@hookflash.com for any bug reports. New feature requests should be directed to erik@hookflash.com.

Thank you for your interest in the Hookflash Open Peer Android SDK.

License:

 Copyright (c) 2014, SMB Phone Inc.
 All rights reserved.
 
 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met:
 
 1. Redistributions of source code must retain the above copyright notice, this
 list of conditions and the following disclaimer.
 2. Redistributions in binary form must reproduce the above copyright notice,
 this list of conditions and the following disclaimer in the documentation
 and/or other materials provided with the distribution.
 
 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 
 The views and conclusions contained in the software and documentation are those
 of the authors and should not be interpreted as representing official policies,
 either expressed or implied, of the FreeBSD Project.
