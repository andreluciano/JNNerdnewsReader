package br.com.andreluciano.jnnerdnewsreader.app.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by André on 02/06/2014.
 */
public class Feed implements Serializable{

    private int _id;
    private int idCategory;
    private String url;
    private String title;
    private String link;
    private String description;
    private String language;
    private String copyright;
    private String pubDate;
    private List<FeedMessage> messages = new ArrayList<FeedMessage>();

    public int getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(int idCategory) {
        this.idCategory = idCategory;
    }

    public String getTitle() {
        return title;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public void setMessages(List<FeedMessage> messages) {
        this.messages = messages;
    }

    public String getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }

    public String getLanguage() {
        return language;
    }

    public String getCopyright() {
        return copyright;
    }

    public String getPubDate() {
        return pubDate;
    }

    public List<FeedMessage> getMessages() {
        return messages;
    }

}