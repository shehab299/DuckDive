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
    //private int SEARCH_WITH_OPERATORS=2;

    @GetMapping
    public List<Result> doSearch(@RequestParam String query) {
        
        if(query.isEmpty() || query.isBlank()) return null;
        //String[] operatorPosition =new String[2];
        if (!(query.startsWith("\"") && query.endsWith("\"")))
        {
            List<String> tokens = Tokenizer.tokenize(query);
            tokens.replaceAll(Tokenizer::stemToken);
            return ranker.rank(tokens,SEARCH_BY_WORD_FLAG,null);
        }
        else //if(!containsOperators(query,operatorPosition))
        {
            return ranker.rank(Tokenizer.tokenize(query),SEARCH_BY_PHRASE_FLAG,null);
        }
        // else if(containsOperators(query,operatorPosition))
        // {
        //     List<String> phrases = extractPhrases(query);
        //     List<String> operators = extractOperators(query);
        //     return ranker.rank(tokens, SEARCH_WITH_OPERATORS,operatorPosition);
        // }
        //return null;
    }
    
    // private boolean containsOperators(String query, String[] operatorPosition) {
    //     return !(extractOperators(query).isEmpty());
    // }

    // public static boolean isInQuotes(String s) {
    //     System.err.println("in isInQoutes");
        
    //         System.err.println("in qoutes");
    //     return false;
    // }

    //     public static List<String> extractPhrases(String input) {
    //     List<String> phrases = new ArrayList<>();
    //     Matcher matcher = Pattern.compile("\"([^\"]*)\"").matcher(input);
    //     while (matcher.find()) {
    //         phrases.add(matcher.group(1));
    //     }
    //     return phrases;
    // }

    // public static List<String> extractOperators(String input) {
    //     List<String> operators = new ArrayList<>();
    //     Matcher matcher = Pattern.compile("\\s+(and|or|not)\\s+").matcher(input);
    //     while (matcher.find()) {
    //         operators.add(matcher.group(1));
    //     }
    //     return operators;
    // }

    

}
