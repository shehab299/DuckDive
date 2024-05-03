package com.crawler.Models;

import org.bson.Document;

public class Page {

    private String url;
    private String originalUrl;
    private String hash;
    private String path;
    private Boolean isIndexed;


    public Page(String url,String o_url, String hash, String path, Boolean isIndexed) {
        this.url = url;
        this.hash = hash;
        this.path = path;
        this.isIndexed = isIndexed;
        this.originalUrl = o_url;
    }

    public  Page(Document pageDoc){
        this.url = pageDoc.getString("url");
        this.hash = pageDoc.getString("hash");
        this.path = pageDoc.getString("path");
        this.isIndexed = pageDoc.getBoolean("isIndexed");
        this.originalUrl = pageDoc.getString("originalUrl");
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHash() {
        return hash;
    }

    public String getOriginalUrl(){
        return originalUrl;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Boolean getIndexed() {
        return isIndexed;
    }

    public void setIndexed(Boolean indexed) {
        isIndexed = indexed;
    }
}
