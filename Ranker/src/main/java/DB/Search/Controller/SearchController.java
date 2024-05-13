package DB.Search.Controller;

import DB.Search.Documents.Result;
import DB.Search.Services.Ranker;
import DB.Search.Utils.Tokenizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/search")
public class SearchController {
    
    @Autowired
    private Ranker ranker;

    @GetMapping
    public List<Result> doSearch(@RequestParam String query) {
        
        if(query.isEmpty() || query.isBlank()) 
            return null;

        String[] words = Tokenizer.splitText(query);

        if(query.contains("\"")){

            query = query.replaceAll("\"", "");
            words = Tokenizer.splitText(query);
            return ranker.searchByPhrase(words);
        }
                
        words = Tokenizer.splitText(query);
        return ranker.searchByWord(words);
    }
}
