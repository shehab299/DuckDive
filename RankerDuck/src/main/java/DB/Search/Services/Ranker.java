package DB.Search.Services;

import DB.Search.Collections.PageCollection;
import DB.Search.Collections.TokenCollection;
import DB.Search.Documents.Page;
import DB.Search.Documents.Result;
import DB.Search.Documents.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class Ranker {
    public static final double H1_TAG_SCORE = 0.5;
    public static final double H2_TAG_SCORE = 0.4;
    public static final double H3_TAG_SCORE = 0.3;
    public static final double P_TAG_SCORE = 0.2;
    public static final double OTHER = 0.1;

    @Autowired
    private PageCollection pageCollection;

    @Autowired
    TokenCollection tokenCollection;

    private Double calculateIDF(String token_){
        
        Optional<Token> token = tokenCollection.findOneByTerm(token_);
        
        if(token.isPresent()){
            return Math.log(pageCollection.count() / token.get().getDocuments().size());
        }

        return 0.0;
    }

    void scorePages(Token token, Double idf, HashMap<String, Double> pageScore){
        token.getDocuments().forEach(document -> {
            Double TF = document.getTF();
            Double score = TF * idf;
            String id = document.getDocid();

            if(pageScore.containsKey(id)){
                pageScore.put(id, pageScore.get(id) + score);
            }else{
                pageScore.put(id, score);
            }
        });
    }


    public List<Result> rank(List<String> tokens){

        HashMap<String, Double> pageScore = new HashMap<String,Double>();

        tokens.forEach(token -> {

            Double idf = calculateIDF(token);
            Optional<Token> token_ = tokenCollection.findOneByTerm(token);

            if(token_.isEmpty()){
                return;
            }

            scorePages(token_.get(), idf, pageScore);
        });   

        List<Result> results = new ArrayList<>();
        
        pageScore.forEach((docid,score) -> {

            Optional<Page> page = pageCollection.findById(docid);
            
            if(page.isEmpty())
                return;

            Result res = new Result();
            res.setUrl(page.get().getUrl());
            res.setScore(score);

            results.add(res);
        });

        results.sort((r1,r2) -> Double.compare(r2.getScore(), r1.getScore()));

        return results;
    }

}