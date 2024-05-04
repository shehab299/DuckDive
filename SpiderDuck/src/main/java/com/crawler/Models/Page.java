package com.crawler.Models;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

public class Page {

    private String url;
    private String originalUrl;
    private String hash;
    private String path;
    private List<String> outlinks;
    private Boolean isIndexed;
    private Boolean isRanked;

    public Page(String url, String o_url, String hash, String path, List<String> extractedOutlinks, Boolean isIndexed,
            Boolean isRanked) {
        this.url = url;
        this.originalUrl = o_url;
        this.hash = hash;
        this.path = path;
        this.outlinks = new ArrayList<>(extractedOutlinks);
        this.isIndexed = isIndexed;
        this.isRanked = isRanked;
    }

    public Page(Document pageDoc) {
        this.url = pageDoc.getString("url");
        this.originalUrl = pageDoc.getString("originalUrl");
        this.hash = pageDoc.getString("hash");
        this.path = pageDoc.getString("path");
        this.outlinks = pageDoc.getList(pageDoc, String.class);
        this.isIndexed = pageDoc.getBoolean("isIndexed");
        this.isRanked = pageDoc.getBoolean("isRanked");
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

    public String getOriginalUrl() {
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

    public Boolean getRanked() {
        return isRanked;
    }

    public List<String> getOutlinks() {
        return outlinks;
    }

    public void setOutlinks(List<String> outlinks) {
        this.outlinks = outlinks;
    }

    public void setRanked(Boolean ranked) {
        isRanked = ranked;
    }

    public void setIndexed(Boolean indexed) {
        isIndexed = indexed;
    }
}
