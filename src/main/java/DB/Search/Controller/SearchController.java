package DB.Search.Controller;

import DB.Search.Documents.Result;
import DB.Search.Services.Ranker;
import DB.Search.Utils.Tokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/search")
public class SearchController {


    @Autowired
    private Ranker ranker;


    @GetMapping
    public List<Result> doSearch(@RequestParam String query) {
        
        List<String> tokens = Tokenizer.tokenize(query);
        tokens.replaceAll(Tokenizer::stemToken);
        
        return ranker.rank(tokens);
    }
    

}
