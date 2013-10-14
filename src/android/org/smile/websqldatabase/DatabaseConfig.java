package org.smile.websqldatabase;

public class DatabaseConfig {
    public String databaseDBName = "Databases.db";
    public String databaseZippedName;
    public String databaseName;
    public String loadingTitle = "Loading";
    public String loadingText = "Loading initial data, please wait...";
    public boolean forceReload = false;

    /**
     * Configuration for the Web SQL Database initializer plugin
     *
     * @param databaseZippedName name of the zip file containing your database
     * @param databaseName       name of the database file in the zip file
     */
    public DatabaseConfig(String databaseZippedName, String databaseName) {
        this.databaseZippedName = databaseZippedName;
        this.databaseName = databaseName;
    }

    /**
     * Configuration for the Web SQL Database initializer plugin, with i18n
     *
     * @param databaseZippedName name of the zip file containing your database
     * @param databaseName       name of the database file in the zip file
     * @param loadingTitle       title of the loading box
     * @param loadingText        text of the loading box
     */
    public DatabaseConfig(String databaseZippedName, String databaseName, String loadingTitle, String loadingText) {
        this.databaseZippedName = databaseZippedName;
        this.databaseName = databaseName;
        this.loadingTitle = loadingTitle;
        this.loadingText = loadingText;
    }

    /**
     * Set the databaseDBName ("Databases.db" by default)
     *
     * @param databaseDBName
     */
    public void setDatabaseDBName(String databaseDBName) {
        this.databaseDBName = databaseDBName;
    }

    public String getDatabaseDBName() {
        return databaseDBName;
    }

    public String getDatabaseZippedName() {
        return databaseZippedName;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public String getLoadingTitle() {
        return loadingTitle;
    }

    public String getLoadingText() {
        return loadingText;
    }

    public boolean isForceReload() {
        return forceReload;
    }

    /**
     * Set forceReload (false by default).
     * If true, the existing database will be overriden by the one provided in "assets".
     * Useful to furnish a new version of the database.
     *
     * @param forceReload
     */
    public void setForceReload(boolean forceReload) {
        this.forceReload = forceReload;
    }
}
