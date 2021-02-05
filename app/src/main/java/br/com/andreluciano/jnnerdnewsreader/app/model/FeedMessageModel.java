package br.com.andreluciano.jnnerdnewsreader.app.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import br.com.andreluciano.jnnerdnewsreader.app.bean.FeedMessage;

/**
 * Created by André on 02/06/2014.
 */
public class FeedMessageModel {

    private static final String TABLE = "feedMessage";
    private static final String[] COLS = {"_id", "idFeed", "title", "description", "contentEnconded", "link", "author", "guid"};

    public FeedMessageModel() {
    }

    public static ArrayList<FeedMessage> listMessages(Context context) {
        ArrayList<FeedMessage> fMessages = new ArrayList<FeedMessage>();

        SQLiteDatabase db = new FeedMessageSQLHelper(context).getReadableDatabase();
        Cursor c = db.query(TABLE, COLS, null, null, null, null, null);

        while (c.moveToNext()) {
            FeedMessage fm = new FeedMessage();
            fm.set_id(c.getInt(c.getColumnIndex("_id")));
            fm.setFeedId(c.getInt(c.getColumnIndex("idFeed")));
            fm.setTitle(c.getString(c.getColumnIndex("title")));
            fm.setDescription(c.getString(c.getColumnIndex("description")));
            fm.setContentEncoded(c.getString(c.getColumnIndex("contentEnconded")));
            fm.setLink(c.getString(c.getColumnIndex("link")));
            fm.setAuthor(c.getString(c.getColumnIndex("author")));
            fm.setGuid(c.getString(c.getColumnIndex("guid")));
            fMessages.add(fm);
        }

        c.close();
        db.close();

        return fMessages;
    }

    public static void insert(Context context, FeedMessage feedMessage) {
        SQLiteDatabase db = new FeedMessageSQLHelper(context).getWritableDatabase();

        ContentValues v = new ContentValues();
        v.put("idFeed", feedMessage.getFeedId());
        v.put("title", feedMessage.getTitle());
        v.put("description", feedMessage.getDescription());
        v.put("contentEnconded", feedMessage.getContentEncoded());
        v.put("link", feedMessage.getLink());
        v.put("author", feedMessage.getAuthor());
        v.put("guid", feedMessage.getGuid());

        db.insert(TABLE, null, v);
        db.close();
    }

    public static void delete(Context context, FeedMessage feedMessage) {
        SQLiteDatabase db = new FeedMessageSQLHelper(context).getWritableDatabase();
        db.delete(TABLE, "_id = " + feedMessage.get_id(), null);
        db.close();
    }

}