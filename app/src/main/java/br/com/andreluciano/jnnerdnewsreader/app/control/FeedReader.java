package br.com.andreluciano.jnnerdnewsreader.app.control;

import android.content.Context;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import br.com.andreluciano.jnnerdnewsreader.app.bean.Feed;
import br.com.andreluciano.jnnerdnewsreader.app.bean.FeedMessage;
import br.com.andreluciano.jnnerdnewsreader.app.model.FeedMessageModel;
import br.com.andreluciano.jnnerdnewsreader.app.model.FeedModel;

/**
 * Created by André on 02/06/2014.
 */
public class FeedReader {

    private Feed feed;
    private String url;
    private List<FeedMessage> feedList;

    public FeedReader(String url) {
        this.feed = new Feed();
        this.feedList = new ArrayList<FeedMessage>();
        this.url = url;
    }

    public void read() {
        try {
            URL url = new URL(this.url);

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(false);

            XmlPullParser xpp = factory.newPullParser();

            // Recupera XML do Source
            xpp.setInput(getInputStream(url), "UTF_8");

            boolean insideChannel = false;
            boolean insideItem = false;
            FeedMessage feedMessage = null;
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {

                if (eventType == XmlPullParser.START_TAG) {

                    if (xpp.getName().equalsIgnoreCase("channel")) {
                        insideChannel = true;
                    }

                    if (insideChannel) {
                        if (insideItem == false) {
                            if (xpp.getName().equalsIgnoreCase("title")) {
                                this.feed.setTitle(xpp.nextText());
                            } else if (xpp.getName().equalsIgnoreCase("description")) {
                                this.feed.setDescription(xpp.nextText());
                            } else if (xpp.getName().equalsIgnoreCase("link")) {
                                this.feed.setLink(xpp.nextText());
                            } else if (xpp.getName().equalsIgnoreCase("pubDate")) {
                                this.feed.setPubDate(xpp.nextText());
                            } else if (xpp.getName().equalsIgnoreCase("item")) {
                                insideItem = true;
                                feedMessage = new FeedMessage();
                            }
                        } else {
                            if (xpp.getName().equalsIgnoreCase("title")) {
                                feedMessage.setTitle(xpp.nextText());
                            } else if (xpp.getName().equalsIgnoreCase("description")) {
                                feedMessage.setDescription(xpp.nextText());
                            } else if (xpp.getName().equalsIgnoreCase("content:encoded")) {
                                feedMessage.setContentEncoded(xpp.nextText());
                            } else if (xpp.getName().equalsIgnoreCase("link")) {
                                feedMessage.setLink(xpp.nextText());
                            } else if (xpp.getName().equalsIgnoreCase("author")) {
                                feedMessage.setAuthor(xpp.nextText());
                            } else if (xpp.getName().equalsIgnoreCase("guid")) {
                                feedMessage.setGuid(xpp.nextText());
                                this.feedList.add(feedMessage);
                            }
                        }
                    }

                } else if (eventType == XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase("item")) {
                    insideItem = false;
                } else if (eventType == XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase("channel")) {
                    this.feed.setMessages(feedList);
                    insideChannel = false;
                }

                eventType = xpp.next();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public InputStream getInputStream(URL url) {
        try {
            return url.openConnection().getInputStream();
        } catch (IOException e) {
            return null;
        }
    }

    public Feed getFeed() {
        return this.feed;
    }

}