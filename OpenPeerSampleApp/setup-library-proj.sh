pushd .
cd assets
<<<<<<< Updated upstream
ln -s ../openpeer-android-sdk/assets/openpeer_database.sql assets/openpeer_database.sql
=======
<<<<<<< Updated upstream
ln -s ../../openpeer-android-sdk/assets/openpeer_database.sql openpeer_database.sql
=======
ln -s ../openpeer-android-sdk/assets/openpeer_database.sql openpeer_database.sql
>>>>>>> Stashed changes
>>>>>>> Stashed changes
popd

rm src/com/openpeer/javaapi
rm src/com/openpeer/sdk
rm jni
rm -rf obj
rm -rf libs/armeabi-v7a
cp .project-library .project
cp .classpath-library .classpath
cp project.properties-library project.properties
