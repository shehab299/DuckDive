package DB.Search.Documents;


public class Result {

    private String url;
    private String snippet;
    private String title;
    private double score;

    public Result(){
        this.score = 0.0;
    }


    public Result(String url, String snippet, String title , double score)
    {
        this.url = url;
        this.snippet = snippet;
        this.title = title;
        this.score = score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public double getScore() {
        return score;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle(){
        return title;
    }
}
