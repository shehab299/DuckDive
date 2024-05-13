package DB.Search.Documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "queries")
public class MyQuery {

    @Id
    private String id;
    private int popularity;
    private String query;

    public MyQuery() {
    }

    public MyQuery(String query, int popularity) {
        this.query = query;
        this.popularity = popularity;
    }

    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id = id;
    }

    public int getPopularity(){
        return popularity;
    }

    public void setPopularity(int popularity){
        this.popularity = popularity;
    }

    public String getQuery(){
        return query;
    }

    public void increasePopularity(){
        popularity++;
    }

    public void setQuery(String query){
        this.query = query;
    }
};
