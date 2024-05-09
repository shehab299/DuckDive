package DB.Search.Controller;

import DB.Search.Collections.TokenCollection;
import DB.Search.Documents.QueryResult;
import DB.Search.Documents.Token;
import DB.Search.Services.Ranker;
import DB.Search.Utils.Tokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;


@RestController
@RequestMapping("/search")
public class SearchController {

    @Autowired
    TokenCollection tokenCollection;

    @Autowired
    private Ranker ranker;


    @GetMapping
    public QueryResult doSearch(@RequestParam String query) {
        
        List<String> tokens = Tokenizer.tokenize(query);
        tokens.replaceAll(Tokenizer::stemToken);
        List<Token> searchTokens = new ArrayList<Token>();

        for (String token : tokens) {

            Optional<Token> tokenDoc = tokenCollection.findOneByTerm(token);

            if(tokenDoc.isPresent())
                searchTokens.add(tokenDoc.get());
        }

        return ranker.rank(0,searchTokens,6000);
    }
    

//    @GetMapping
//    public Optional<Page> getPage(@RequestParam String id){
//
//        System.out.println(id);
//
//        return pageCollection.findById(new ObjectId(id));
//
//    }

//    @GetMapping
//    public List<Doc> search(@RequestParam String query){
//
//        //Get all documents associated with a query
//        Optional<Token> token = tokenCollection.findOneByTerm(query);
//        return token.map(Token::getDocuments).orElse(null);
//
//        // query -> do query processing on query -> list of tokens
//        // list of tokens ->
//        // for each token get the pages associated with each token
//        // assign them scores
//        // sort them
//        // put them on the jsonResponse and send it
//
//    }

}
