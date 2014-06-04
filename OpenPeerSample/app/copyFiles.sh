OP_SDK_PATH=/Users/brucexia/opandroid/openpeer-android-sdk/bin
OP_NATIVESAMPLE_PATH=/Users/brucexia/opandroid/OPenPeerNativeSampleApp/src/com/openpeer/delegates
SRC_PATH=./src/main/java/com/openpeer/sample/app
rm -rf $SRC_PATH/delegates/*
rm -rf ./src/com/openpeer/delegates/

#cp -r $OP_NATIVESAMPLE_PATH/[^Callback]* $SRC_PATH/delegates/
cp $OP_SDK_PATH/openpeer-android-sdk.jar ./libs/
#rm -rf ./src/com/openpeer/javaapi/test/