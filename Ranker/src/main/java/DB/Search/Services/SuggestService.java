package DB.Search.Services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import DB.Search.Collections.QueryCollection;
import DB.Search.Documents.MyQuery;

@Service
public class SuggestService {


    @Autowired
    QueryCollection queryCollection;

    public void updateSuggestions(String query) {

        Optional<MyQuery> queryDoc = queryCollection.findOneByQuery(query);

        if(queryDoc.isPresent()){
            MyQuery q = queryDoc.get();
            q.increasePopularity();
            queryCollection.save(q);
        } else {
            MyQuery newQuery = new MyQuery(query, 1);
            queryCollection.save(newQuery);
        }

    }

    
}
