This is a Cordova 3 plugin (using the new plugin management system) that enables initialization for WebSqlDatabase.

If you want your PhoneGap application to start with a preloaded WebSqlDatabase, this plugin will simplify it for you.

This plugin is compatible with iOS, Android and WP (7 & 8).


## Install
Using the Cordova 3 Command-line Interface:

```sh
cordova plugin add org.smile.websqldatabase.initializer
```

If you are not using the Cordova Command-line Interface, follow [Using Plugman to Manage Plugins](http://cordova.apache.org/docs/en/edge/plugin_ref_plugman.md.html).


## Create the database containing initialization data

The database used for initialization (named "myDemoSQLiteDB" below) is an SQLite file with a ".db" extension compressed in a ".zip" file.

You can create the ".db" file with the initial content via Chrome/Chromium using the WebSqlDatabase API (and retrieve it from the "databases" diretory of the [User Data Directory](http://www.chromium.org/user-experience/user-data-directory)) or with an SQLite client (sqlite3, sqlitebrowser, ...).

Name the resulting database binary file with a ".db" extension and zip it.

## Databases.db

For iOS and Android, a Databases.db file is required to "index" the available databases.

To create this file, use an SQLite client (sqlite3, sqlitebrowser, ...) and execute the following code (replacing "myDemoSQLiteDB.db" with the correct value):

```sql
CREATE TABLE Databases (guid INTEGER PRIMARY KEY AUTOINCREMENT, origin TEXT, name TEXT, displayName TEXT, estimatedSize INTEGER, path TEXT);
INSERT INTO Databases VALUES(1, 'file__0', 'myDemoSQLiteDB.db', 'Proto DB', 1000000, 'myDemoSQLiteDB.db');
CREATE TABLE Origins (origin TEXT UNIQUE ON CONFLICT REPLACE, quota INTEGER NOT NULL ON CONFLICT FAIL);
INSERT INTO Origins VALUES('file__0',52428800);
```

## Usage
### Android

  * import "org.smile.websqldatabase.*" in your main class:

```java
import org.smile.websqldatabase.*;
```

  * add "implements DatabaseInitializable" on your main class
  * implement the getDatabaseConfig method (with the correct database configuration):

```java
@Override
public DatabaseConfig getDatabaseConfig() {
    return new DatabaseConfig("myDemoSQLiteDB.zip", "myDemoSQLiteDB.db");
}
```

  * implement the loadWebApp method (with the Cordova init code usually in the onCreate method)

```java
@Override
public void loadWebApp() {
    super.loadUrl(Config.getStartUrl());
}
```

  * remove the Cordova init code in the onCreate method
  * load the database and run the app with "DatabaseInitializer.load(this);" in the onCreate method

```java
@Override
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    super.init();
    DatabaseInitializer.load(this);
}
```

  * add the "Databases.db" file in your assets directory
  * add the zipped database ("myDemoSQLiteDB.zip") in your assets directory

### iOS

  * import "LoadDatabase.h" and "DatabaseConfig.h" in your main class

```objective-c
#import "LoadDatabase.h"
#import "DatabaseConfig.h"
```

  * in the init method of your main class, call the "load" method on a "LoadDatabase" object with the correct DatabaseConfig in parameters:

```objective-c
[[LoadDatabase new] load:[[DatabaseConfig alloc] init:@"myDemoSQLiteDB.zip" secondValue:@"myDemoSQLiteDB.db"]];
```

  * add the "Databases.db" file in your Resources directory
  * add the zipped database ("myDemoSQLiteDB.zip") in your Resources directory
  
### Windows Phone (7 & 8)

  * check that the http://smile-sa.github.io/cordova-plugin-websqldatabase/ plugin (enabling WebSqlDatabase feature on Windows Phone) has been correctly installed (check that the "plugins/org.smile.websqldatabase.wpdb" directory exists)
  * import "org.smile.websqldatabase" in your main class:

```csharp
using org.smile.websqldatabase;
```

  * in the init method of your main class, call the "LoadDatabase" method on a "DatabaseLoader" object with the correct DatabaseConfig in parameters:

```csharp
new DatabaseLoader().LoadDatabase(new DatabaseConfig(@"myDemoSQLiteDB.zip", @"myDemoSQLiteDB.db"));
```

  * add the zipped database ("myDemoSQLiteDB.zip") in the root directory of your solution
  * no need of a "Databases.db" file


## Usage in JavaScript

Load the database with the following code:

```javascript
window.openDatabase("myDemoSQLiteDB.db", "1.0", "Proto DB", 1000000);
```

  
## Example

A prototype including this plugin is available at https://github.com/Smile-SA/cordova-plugin-websqldatabase-initializer-proto

## External library

This plugin uses the following libraries:
  * [ZipArchive](http://code.google.com/p/ziparchive/) (under MIT License) for iOS
  * [SharpGIS.UnZipper](http://www.sharpgis.net/post/2010/08/25/REALLY-small-unzip-utility-for-Silverlight-e28093-Part-2.aspx) (under Microsoft Public License (Ms-PL)) for Windows Phone