package DB.Search.Documents;

import java.util.List;

public class Doc {
    private String docid;
    private String html_pos;
    private Double TF;
    private List<Integer> pos;
    private Double score;

    @Override
    public String toString() {
        return "Doc{" +
                "docid='" + docid + '\'' +
                ", html_pos='" + html_pos + '\'' +
                ", TF=" + TF +
                ", pos=" + pos +
                '}';
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public String getDocid() {
        return docid;
    }

    public void setId(String docid) {
        this.docid = docid;
    }

    public Double getTF() {
        return TF;
    }

    public void setTF(Double TF) {
        this.TF = TF;
    }

    public List<Integer> getPositions() {
        return pos;
    }

    public void setPositions(List<Integer> positions) {
        this.pos = positions;
    }

    public Doc() {
    }

    public Doc(String docid, Double TF, List<Integer> pos, String html_pos) {
        this.pos = pos;
        this.docid = docid;
        this.TF = TF;
        this.html_pos = html_pos;
    }

    public String getHtml_pos() {
        return html_pos;
    }

    public void setHtml_pos(String html_pos) {
        this.html_pos = html_pos;
    }


}
