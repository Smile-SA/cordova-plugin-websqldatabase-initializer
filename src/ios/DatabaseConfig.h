@interface DatabaseConfig : NSObject

{
    NSString *databaseDBName;
    NSString *databaseZippedName;
    NSString *databaseName;
    BOOL forceReload;
}

- (id) init:(NSString*) databaseZippedName secondValue:(NSString*) databaseName;
- (void) databaseDBName:(NSString*) databaseDBName;
- (NSString*) databaseDBName;
- (NSString*) databaseZippedName;
- (NSString*) databaseName;
- (BOOL) forceReload;
- (void) forceReload:(BOOL) forceReload;
@end