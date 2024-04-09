package com.crawler;

public class Page {

    private String url;
    private String hash;
    private String path;
    private Boolean isIndexed;
    private String lastModified;

    public Page(String url, String hash, String path, Boolean isIndexed, String lastModified) {
        this.url = url;
        this.hash = hash;
        this.path = path;
        this.isIndexed = isIndexed;
        this.lastModified = lastModified;
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

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }
}
