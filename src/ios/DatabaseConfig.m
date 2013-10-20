#import "DatabaseConfig.h"

@implementation DatabaseConfig : NSObject

NSString *databaseDBName;
NSString *databaseZippedName;
NSString *databaseName;
BOOL forceReload = false;


/**
 * Configuration for the Web SQL Database initializer plugin
 *
 * @param databaseZippedName name of the zip file containing your database
 * @param databaseName       name of the database file in the zip file
 */
- (id) init:(NSString*) databaseZippedNameValue secondValue:(NSString*) databaseNameValue
{
    if(self = [super init])
    {
        databaseDBName = @"Databases.db";

        databaseZippedName = databaseZippedNameValue;
        databaseName = databaseNameValue;
    }
    return self;
}

/**
 * Set the databaseDBName ("Databases.db" by default)
 *
 * @param databaseDBName
 */
- (void) databaseDBName:(NSString*) databaseDBNameValue
{
    databaseDBName = databaseDBNameValue;
}

- (NSString*) databaseDBName
{
    return databaseDBName;
}

- (NSString*) databaseZippedName
{
    return databaseZippedName;
}

- (NSString*) databaseName
{
    return databaseName;
}

- (BOOL) forceReload
{
    return forceReload;
}

/**
 * Set forceReload (false by default).
 * If true, the existing database will be overriden by the one provided in "assets".
 * Useful to furnish a new version of the database.
 *
 * @param forceReload
 */
- (void) forceReload:(BOOL) forceReloadValue
{
    forceReload = forceReloadValue;
}
@end