package DB.Search.Documents;

import java.util.List;

public class QueryResult {
    private List<String> docPath;
    private List<String> urls;
    private List<String> snippet;
    private List<String> titles;


    public QueryResult(QueryResult result)
    {
        if(result==null) return;
        this.docPath = result.docPath;
        this.urls = result.urls;
        this.snippet = result.snippet;
        this.titles = result.titles;
    }

    public void setDocPath(List<String> docPath) {
        this.docPath = docPath;
    }

    public void setSnippet(List<String> snippet) {
        this.snippet = snippet;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    public List<String> getDocPath() {
        return docPath;
    }

    
    public List<String> getUrls() {
        return urls;
    }

    public void setTitles(List<String> titles) {
        this.titles = titles;
    }

    public List<String> getTitles(){
        return titles;
}
}
