COMMAND_START_CONTACTS="adb -d shell am start -n com.openpeer.sample/.contacts/ContactsActivity -d test/test"  
  
  
Runtime.getRuntime().exec(COMMAND_START_CONTACTS)  