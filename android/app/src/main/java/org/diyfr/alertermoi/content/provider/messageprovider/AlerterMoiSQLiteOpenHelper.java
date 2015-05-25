package org.diyfr.alertermoi.content.provider.messageprovider;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.DefaultDatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import org.diyfr.alertermoi.content.provider.messageprovider.messages.MessagesColumns;
import org.diyfr.alertermoi.BuildConfig;

public class AlerterMoiSQLiteOpenHelper extends SQLiteOpenHelper {
    private static final String TAG = AlerterMoiSQLiteOpenHelper.class.getSimpleName();

    public static final String DATABASE_FILE_NAME = "pushpersos.db";
    private static final int DATABASE_VERSION = 1;

    // @formatter:off
    private static final String SQL_CREATE_TABLE_MESSAGES = "CREATE TABLE IF NOT EXISTS "
            + MessagesColumns.TABLE_NAME + " ( "
            + MessagesColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + MessagesColumns.MESSAGE_TYPE + " TEXT, "
            + MessagesColumns.MESSAGE_TITLE + " TEXT NOT NULL, "
            + MessagesColumns.MESSAGE_SUBTITLE + " TEXT NOT NULL, "
            + MessagesColumns.MESSAGE_ICON + " TEXT, "
            + MessagesColumns.MESSAGE_CONTENT + " TEXT NOT NULL, "
            + MessagesColumns.MESSAGE_DATE_EMISSION + " INTEGER NOT NULL, "
            + MessagesColumns.MESSAGE_DATE_RECEPTION + " INTEGER NOT NULL, "
            + MessagesColumns.MESSAGE_LU + " INTEGER NOT NULL, "
            + MessagesColumns.MESSAGE_LEVEL + " INTEGER NOT NULL "
            + " );";

    // @formatter:on

    public static AlerterMoiSQLiteOpenHelper newInstance(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            return newInstancePreHoneycomb(context);
        }
        return newInstancePostHoneycomb(context);
    }


    /*
     * Pre Honeycomb.
     */

    private static AlerterMoiSQLiteOpenHelper newInstancePreHoneycomb(Context context) {
        return new AlerterMoiSQLiteOpenHelper(context, DATABASE_FILE_NAME, null, DATABASE_VERSION);
    }

    private AlerterMoiSQLiteOpenHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    /*
     * Post Honeycomb.
     */

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private static AlerterMoiSQLiteOpenHelper newInstancePostHoneycomb(Context context) {
        return new AlerterMoiSQLiteOpenHelper(context, DATABASE_FILE_NAME, null, DATABASE_VERSION, new DefaultDatabaseErrorHandler());
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private AlerterMoiSQLiteOpenHelper(Context context, String name, CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        if (BuildConfig.DEBUG) Log.d(TAG, "onCreate");
        db.execSQL(SQL_CREATE_TABLE_MESSAGES);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (BuildConfig.DEBUG) Log.d(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion);
    }
}
