# cordova-plugin-websqldatabase-initializer
To install this plugin, follow the [Command-line Interface Guide](http://cordova.apache.org/docs/en/edge/guide_cli_index.md.html#The%20Command-line%20Interface).

If you are not using the Cordova Command-line Interface, follow [Using Plugman to Manage Plugins](http://cordova.apache.org/docs/en/edge/plugin_ref_plugman.md.html).


# Configuration
## Android
You need to:

   * add "implements DatabaseInitializable" on your main class
   * implement the getDatabaseConfig method (with the correct database configuration)
   * implement the loadWebApp method (with the Cordova init code usually in the onCreate method)
   * remove the Cordova init code in the onCreate method
   * load the database and run the app with "DatabaseInitializer.load(this);" in the onCreate method
   * add the "Databases.db" file in your assets directory
   * add the zipped database to initialize in your assets directory

## iOS
You need to:

  * import "LoadDatabase.h" and "DatabaseConfig.h" in your main class
  * in the init method of your main class, call the "load" method on a "LoadDatabase" object with the correct DatabaseConfig in parameters:


    [[LoadDatabase new] load:[[DatabaseConfig alloc] init:@"myDemoSQLiteDB.zip" secondValue:@"myDemoSQLiteDB.db"]];
  * add the "Databases.db" file in your Resources directory
  * add the zipped database to initialize in your Resources directory