package org.smile.websqldatabase;

public interface DatabaseInitializable {
    /**
     * To get the database configuration
     */
    DatabaseConfig getDatabaseConfig();

    /**
     * To listen on the database loaded event
     */
    void loadWebApp();
}
