#import "LoadDatabase.h"
#import "DatabaseConfig.h"
#import "ZipArchive/ZipArchive.h"

@implementation LoadDatabase : NSObject 

NSString *databasePath;
NSString *masterFile;

- (void)load:(DatabaseConfig*) databaseConfig
{
    [self computePaths:databaseConfig];

    if([self alreadyExists:databaseConfig] && ![databaseConfig forceReload]){
        NSLog(@"Database already exists");
    } else {
        if([databaseConfig forceReload]) NSLog(@"Overriding existing database");
        [self copyDatabase:databaseConfig];
    }
}

- (void)computePaths:(DatabaseConfig*) databaseConfig
{
    NSArray *libraryPaths = NSSearchPathForDirectoriesInDomains(NSLibraryDirectory, NSUserDomainMask, YES);
    NSString *libraryDir = [libraryPaths objectAtIndex:0];
    
    NSString *basePath = nil;
    float iOSVersion = [UIDevice currentDevice].systemVersion.floatValue;
    NSLog(@"iOS version: %.1f", iOSVersion);
    if(iOSVersion >= 6.0f) {
        basePath = [libraryDir stringByAppendingPathComponent:@"WebKit/LocalStorage/"];
    } else if(iOSVersion >= 5.1f) {
        //In iOS 5.1, databases are stored in "Caches" directory (so data can be lost if caches are cleared by the OS). But Cordova (since 1.6.0 version) does an automatic backup in the "Documents" directory. The backup is automatically restored if caches are cleared. The downside is that the database is in 3 versions (in the downloaded App, in the Caches directory and in the Documents directory).
        basePath = [libraryDir stringByAppendingPathComponent:@"Caches/"];
    } else {
        basePath = [libraryDir stringByAppendingPathComponent:@"WebKit/Databases/"];
    }
    NSLog(@"Path to database: %@", basePath);
    
    databasePath = [basePath stringByAppendingPathComponent:@"file__0/"];
    masterFile = [basePath stringByAppendingPathComponent:[databaseConfig databaseDBName]];
}

- (BOOL)alreadyExists:(DatabaseConfig*) databaseConfig
{
    return [[NSFileManager defaultManager] fileExistsAtPath:[databasePath stringByAppendingPathComponent:[databaseConfig databaseName]]];
}
    

- (void)copyDatabase:(DatabaseConfig*) databaseConfig
{
    NSLog(@"Copying database");
    NSFileManager *fileManager = [NSFileManager defaultManager];
    
    NSString *databasePathFromApp = [[[NSBundle mainBundle] resourcePath] stringByAppendingPathComponent:[databaseConfig databaseZippedName]];
    NSString *masterPathFromApp = [[[NSBundle mainBundle] resourcePath] stringByAppendingPathComponent:[databaseConfig databaseDBName]];
    
    [fileManager createDirectoryAtPath:databasePath withIntermediateDirectories:YES attributes:nil error:NULL];
    
    ZipArchive* za = [[ZipArchive alloc] init];
    if ([za UnzipOpenFile: databasePathFromApp]) {
        BOOL ret = [za UnzipFileTo: databasePath overWrite: YES];
        if (NO == ret){} [za UnzipCloseFile];
    }
    
    [fileManager copyItemAtPath:masterPathFromApp toPath:masterFile error:nil];
}

@end