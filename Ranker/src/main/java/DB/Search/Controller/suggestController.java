package DB.Search.Controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import DB.Search.Collections.QueryCollection;
import DB.Search.Documents.MyQuery;



@RestController
@RequestMapping("/suggest")
public class suggestController {

    @Autowired
    private QueryCollection queryCollection;

    @GetMapping
    public List<String> suggest(@RequestParam String query) {

        List<String> suggestions = new ArrayList<>();

        if(query.isEmpty() || query.isBlank()) 
            return suggestions;

        String qregex = query;
        List<MyQuery> queries = queryCollection.findQueryByRegex(qregex);

        queries.sort((q1, q2) -> q2.getPopularity() - q1.getPopularity());

        int counter = 0;
        for(MyQuery q : queries){
            
            if(counter++ == 5)
                break;
            
            suggestions.add(q.getQuery());
            counter++;
        }


        return suggestions;


    }    
}
