rm -rf ./src/com/
#rm -rf ./src/com/openpeer/delegates/

mkdir -p ./src/com/openpeer/javaapi/
mkdir -p ./src/com/openpeer/delegates/

cp -r ./../OpenPeerNativeSampleApp/src/com/openpeer/javaapi/* ./src/com/openpeer/javaapi/
cp -r ./../OpenPeerNativeSampleApp/src/com/openpeer/delegates/Callback* ./src/com/openpeer/delegates/