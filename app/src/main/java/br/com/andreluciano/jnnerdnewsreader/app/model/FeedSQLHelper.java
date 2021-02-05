package br.com.andreluciano.jnnerdnewsreader.app.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by André on 02/06/2014.
 */
public class FeedSQLHelper extends SQLiteOpenHelper {

    private static final String TABLE = "feed";
    private static final int VERSION = 1;

    public FeedSQLHelper(Context context) {
        super(context, TABLE, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE + " ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "idCategory int,"
                + "url text,"
                + "title text,"
                + "link text,"
                + "description text,"
                + "language text,"
                + "copyright text,"
                + "pubDate text"
                + ");";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

}