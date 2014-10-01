package org.smile.websqldatabase;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.*;
import java.lang.String;
import java.util.zip.ZipInputStream;

public class AsyncLoadDatabase extends AsyncTask<Void, Integer, Void> {
    private static final String TAG = "AsyncLoadDatabase";

    private static final boolean KITKAT_OR_ABOVE = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT);
    private static final String DATABASE_FOLDER = KITKAT_OR_ABOVE ? "app_webview/databases/" : "app_database/";

    private ProgressDialog progressDialog;

    private Context context;
    private DatabaseInitializable app;
    private static DatabaseConfig config;

    public AsyncLoadDatabase(DatabaseInitializable obj) {
        this.context = ((Activity) obj).getApplicationContext().getApplicationContext();
        this.app = obj;
        this.config = app.getDatabaseConfig();
    }

    @Override
    protected Void doInBackground(Void... params) {
        final String dbPath = getDatabasePath(context, config);
        final String dbsDbPath = getDatabasesDBPath(context, config);

        try {
            File databaseFile = new File(dbPath);
            File databasesDBFile = new File(dbsDbPath);

            if (databaseFile.exists()) {
                Log.d(TAG, "Deleting file:" + dbPath);
                databaseFile.delete();
            }
            if (databasesDBFile.exists()) {
                Log.d(TAG, "Deleting file:" + dbsDbPath);
                databasesDBFile.delete();
            }

            copyFromAsset(config.getDatabaseZippedName(), dbPath);
            copyFromAsset(config.getDatabaseDBName(), dbsDbPath);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void copyFromAsset(String assetSource, String destination) throws IOException {
        createFolderIfNotExist(destination);

        InputStream in = null;
        OutputStream out = null;

        try {
            in = context.getAssets().open(assetSource);

            // Handling zipped database
            if (assetSource.endsWith("zip")) {
                Log.v(TAG, "Unzipping file: " + assetSource);
                in = new ZipInputStream(in);
                ((ZipInputStream) in).getNextEntry(); // "Reads the next ZIP file entry and positions the stream at the beginning of the entry data."
            }

            out = new FileOutputStream(destination);
            // Transfer bytes from in to out
            Log.v(TAG, "Copying file " + assetSource + " to " + destination);
            byte[] buf = new byte[1024];
            int len;

            while ((len = in.read(buf)) > 0)
                out.write(buf, 0, len);

        } catch (IOException e) {
            throw e;
        } finally {
            closeSilently(in);
            closeSilently(out);
        }

    }

    private void closeSilently(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
            }
        }
    }

    @Override
    protected void onPreExecute() {
        Log.v(TAG, "Opening waiting dialog");
        progressDialog = ProgressDialog.show((Context) app, config.getLoadingTitle(),
                config.getLoadingText(), true, false);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (progressDialog != null) {
            Log.v(TAG, "Closing waiting dialog");
            progressDialog.dismiss();
            progressDialog = null;
        }

        if (app != null) {
            Log.v(TAG, "Launching app");
            app.loadWebApp();
        }
    }

    private static void createFolderIfNotExist(String filePath) {
        Log.v(TAG, "Creating directory (if needed) for file: " + filePath);
        File file = new File(filePath);
        file.getParentFile().mkdirs();
    }

    private static String getDatabasePath(Context context, DatabaseConfig config) {
        String databaseName = KITKAT_OR_ABOVE ? "1": config.getDatabaseName();
        String databasePath = context.getApplicationInfo().dataDir + "/" + DATABASE_FOLDER + "file__0/" + databaseName;
        Log.v(TAG, "databasePath: " + databasePath);
        return databasePath;
    }

    private static String getDatabasesDBPath(Context context, DatabaseConfig config) {
        String databasesDBPath = context.getApplicationInfo().dataDir + "/" + DATABASE_FOLDER + config.getDatabaseDBName();
        Log.v(TAG, "databasesDBPath: " + databasesDBPath);
        return databasesDBPath;
    }

    public static boolean shouldLoadDatabase(DatabaseInitializable app) {
        // TODO: Check the files size in case of incomplete copy (whatever the reason)?
        Context context = (Context) app;
        DatabaseConfig config = app.getDatabaseConfig();

        boolean databaseExists = new File(getDatabasePath(context, config)).exists() && new File(getDatabasesDBPath(context, config)).exists();
        boolean forceReload = config.isForceReload();

        if(forceReload) {
            Log.d(TAG, "ForceReload is true: the database will be overriden by the one in assets directory.");
        } else if(!databaseExists) {
            Log.d(TAG, "No local database: the database will be created from the one in assets directory.");
        } else {
            Log.d(TAG, "The local database already exists.");
        }

        return forceReload || !databaseExists;
    }
}
