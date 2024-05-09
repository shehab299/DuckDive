package DB.Search.Documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "page")
public class Page {

    @Id
    String id;
    String url;
    String hash;
    String path;
    String doc_path;
    Boolean is_indexed;

    public Page() {
    }

    public Page(String id, String url, String hash, String path, Boolean indexed, String doc_path) {
        this.id = id;
        this.url = url;
        this.hash = hash;
        this.path = path;
        this.is_indexed = indexed;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Boolean getIs_indexed() {
        return is_indexed;
    }

    public void setIs_indexed(Boolean is_indexed) {
        this.is_indexed = is_indexed;
    }

    public String getDoc_path() {
        return doc_path;
    }

    public void setDoc_path(String doc_path) {
        this.doc_path = doc_path;
    }
}

//
//          "url": "https://en.wikipedia.org/",
//          "hash": "f93cd80de2d57f1ed6a46d8e29068864964d88cdd817e54682c2c3c6bc294c02",
//          "path": "/home/shehab/DuckDive/SpiderDuck/Resources/HtmlPages/0.html",
//          "is_indexed": false
