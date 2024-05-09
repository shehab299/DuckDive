package DB.Search.Documents;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "InvertedIndex")
public class Token {

    @Id
    private String id;
    private String term;
    private List<Doc> documents;


    public Token(String id, String term) {
        this.id = id;
        this.term = term;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public List<Doc> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Doc> documents) {
        this.documents = documents;
    }

    @Override
    public String toString() {
        return "Token{" +
                "Id='" + this.id + '\'' +
                ", term='" + term + '\'' +
                ", documents=" + documents +
                '}';
    }
}
