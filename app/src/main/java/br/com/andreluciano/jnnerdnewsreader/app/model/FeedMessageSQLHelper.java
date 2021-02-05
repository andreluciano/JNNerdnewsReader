package br.com.andreluciano.jnnerdnewsreader.app.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by André on 02/06/2014.
 */
public class FeedMessageSQLHelper extends SQLiteOpenHelper{

    private static final String TABLE = "feedMessage";
    private static final int VERSION = 1;

    public FeedMessageSQLHelper(Context context) {
        super(context, TABLE, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE + " ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "feedId int,"
                + "title text,"
                + "description text,"
                + "contentEnconded text,"
                + "link text,"
                + "author text,"
                + "guid text"
                + ");";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

}