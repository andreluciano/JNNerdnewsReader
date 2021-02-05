package br.com.andreluciano.jnnerdnewsreader.app.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import br.com.andreluciano.jnnerdnewsreader.app.bean.Feed;

/**
 * Created by André on 02/06/2014.
 */
public class FeedModel {

    private static final String TABLE = "feed";
    private static final String[] COLS = {"_id", "idCategory", "url", "title", "link", "description", "language", "copyright", "pubDate"};

    public FeedModel() {
    }

    public static ArrayList<Feed> listFeeds(Context context) {
        ArrayList<Feed> feeds = new ArrayList<Feed>();

        SQLiteDatabase db = new FeedSQLHelper(context).getReadableDatabase();
        Cursor c = db.query(TABLE, COLS, null, null, null, null, null);

        while (c.moveToNext()) {
            Feed f = new Feed();
            f.set_id(c.getInt(c.getColumnIndex("_id")));
            f.setIdCategory(c.getInt(c.getColumnIndex("idCategory")));
            f.setUrl(c.getString(c.getColumnIndex("url")));
            f.setTitle(c.getString(c.getColumnIndex("title")));
            f.setLink(c.getString(c.getColumnIndex("link")));
            f.setDescription(c.getString(c.getColumnIndex("description")));
            f.setLanguage(c.getString(c.getColumnIndex("language")));
            f.setCopyright(c.getString(c.getColumnIndex("copyright")));
            f.setPubDate(c.getString(c.getColumnIndex("pubDate")));
            feeds.add(f);
        }

        c.close();
        db.close();

        return feeds;
    }

    public static void insert(Context context, Feed feed) {
        SQLiteDatabase db = new FeedSQLHelper(context).getWritableDatabase();

        ContentValues v = new ContentValues();
        v.put("url", feed.getUrl());
        v.put("idCategory", feed.getIdCategory());
        v.put("title", feed.getTitle());
        v.put("link", feed.getLink());
        v.put("description", feed.getDescription());
        v.put("language", feed.getLanguage());
        v.put("copyright", feed.getCopyright());
        v.put("pubDate", feed.getPubDate());

        db.insert(TABLE, null, v);
        db.close();
    }

    public static void delete(Context context, Feed feed) {
        SQLiteDatabase db = new FeedSQLHelper(context).getWritableDatabase();
        db.delete(TABLE, "_id = " + feed.get_id(), null);
        db.close();
    }

}