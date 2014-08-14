rm -rf ./src/com/
rm -rf ./jni/
#rm -rf ./src/com/openpeer/delegates/

mkdir -p ./jni/
mkdir -p ./src/com/openpeer/javaapi/
mkdir -p ./src/com/openpeer/delegates/

cp -r ./../OpenPeerNativeSampleApp/jni/* ./jni/
cp -r ./../OpenPeerNativeSampleApp/src/com/openpeer/javaapi/* ./src/com/openpeer/javaapi/
cp -r ./../OpenPeerNativeSampleApp/src/com/openpeer/delegates/Callback* ./src/com/openpeer/delegates/

rm -rf ./src/com/openpeer/javaapi/test/