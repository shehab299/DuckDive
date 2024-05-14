package DB.Search.Controller;

import DB.Search.Documents.Result;
import DB.Search.Services.Ranker;
import DB.Search.Services.SuggestService;
import DB.Search.Utils.Tokenizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/search")
public class SearchController {
    
    @Autowired
    private SuggestService suggestService;

    @Autowired
    private Ranker ranker;  

    @GetMapping
    public List<Result> search(@RequestParam String query) {
        
        if(query.isEmpty() || query.isBlank()) 
            return null;

        

        String[] words = Tokenizer.splitText(query);

        if(query.contains("\"")){
            int size = query.length() - 2;
            query = query.replaceAll("\"", "");
            words = Tokenizer.splitText(query);
            return ranker.searchByPhrase(words,size);
        }
                
        suggestService.updateSuggestions(query);
        words = Tokenizer.splitText(query);
        return ranker.searchByWord(words);
    }
}
