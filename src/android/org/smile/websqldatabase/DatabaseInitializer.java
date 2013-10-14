package org.smile.websqldatabase;

import android.util.Log;

public abstract class DatabaseInitializer {
    private static final String TAG = "DatabaseInitializer";

    /**
     * Method to call to launch Cordova application with automatic database initialization
     */
    public static void load(DatabaseInitializable app){
        boolean shouldLoadDatabase = AsyncLoadDatabase.shouldLoadDatabase(app);

        if (shouldLoadDatabase) {
            new AsyncLoadDatabase(app).execute();
        } else {
            Log.v(TAG, "Launching app");
            app.loadWebApp();
        }
    }
}
