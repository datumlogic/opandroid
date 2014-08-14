PACKAGE_NAME=com.openpeer.sample
if [ ! -z $1 ];then
ACTIVITY=$1
fi
if [ ! -z $2 ]; then
DATA=$2
else
DATA=test
fi
adb -d shell am start -n com.openpeer.sample/$ACTIVITY -d $$DATA
  
#Runtime.getRuntime().exec(COMMAND_START_CONTACTS)