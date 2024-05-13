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
    private int SEARCH_BY_WORD_FLAG=0;
    private int SEARCH_BY_PHRASE_FLAG=1;

    @GetMapping
    public List<Result> doSearch(@RequestParam String query) {
        
        if(query.isEmpty() || query.isBlank()) 
            return null;
        
        System.out.println(query);

        String[] words = Tokenizer.splitText(query);

        for (String words2 : words) {
            System.out.println(words2);
        }

        // return ranker.searchByWord(words, SEARCH_BY_WORD_FLAG);

        return ranker.searchByPhrase(words);
    }
}
