package DB.Search.Controller;

import DB.Search.Documents.Result;
import DB.Search.Services.Ranker;
import DB.Search.Services.SuggestService;
import DB.Search.Utils.Tokenizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            List<String> operators= new ArrayList<>();
            List<String> phrases= new ArrayList<>();
            boolean containsOp= extractStringsAndOperators(query,operators,phrases);
            query = query.replaceAll("\"", "");
            words = Tokenizer.splitText(query);
            if(containsOp)
            {
                List<String[]> phrasesWords=new ArrayList<>();
                for (String phrase : phrases) {
                    phrasesWords.add(Tokenizer.splitText(phrase.replaceAll("\"", "")));
                }
                return ranker.operatorHandler(phrasesWords,operators);
            }
            return ranker.searchByPhrase(words);
        }
                
        suggestService.updateSuggestions(query);
        words = Tokenizer.splitText(query);
        return ranker.searchByWord(words);
    }

    private static boolean extractStringsAndOperators(String inputString, List<String> operators, List<String> phrases) {
        String pattern1 = "\"([^\"]+)\"\\s+(and|or|not)\\s+\"([^\"]+)\"\\s+(and|or|not)\\s+\"([^\"]+)\"";
        String pattern2 = "\"([^\"]+)\"\\s+(and|or|not)\\s+\"([^\"]+)\"";
        
        Matcher matcher1 = Pattern.compile(pattern1,Pattern.CASE_INSENSITIVE).matcher(inputString);
        if (matcher1.find()) {
            phrases.add(matcher1.group(1));
            phrases.add(matcher1.group(3));
            phrases.add(matcher1.group(5));
            operators.add(matcher1.group(2));
            operators.add(matcher1.group(4));
            return true;
        } else {
            Matcher matcher2 = Pattern.compile(pattern2,Pattern.CASE_INSENSITIVE).matcher(inputString);
            if (matcher2.find()) {
                phrases.add(matcher2.group(1));
                phrases.add(matcher2.group(3));
                operators.add(matcher2.group(2));
                return true;
            } else {
                return false;
            }
        }
    }

}
