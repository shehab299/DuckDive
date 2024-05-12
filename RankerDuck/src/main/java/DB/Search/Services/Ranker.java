package DB.Search.Services;

import DB.Search.Collections.PageCollection;
import DB.Search.Collections.TokenCollection;
import DB.Search.Documents.Doc;
import DB.Search.Documents.Page;
import DB.Search.Documents.Result;
import DB.Search.Documents.Token;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Ranker {
    public static final double H1_TAG_SCORE = 0.5;
    public static final double H2_TAG_SCORE = 0.4;
    public static final double H3_TAG_SCORE = 0.3;
    public static final double P_TAG_SCORE = 0.2;
    public static final double OTHER = 0.1;
    public static final int SNIPPET_LENGTH = 150;
    public static int TYPE;
    
    List<Token> searchTokens;
    HashMap<String, Double> pageScore;
    List<Result> results;
    String searchPhrase;
    List<Integer> phrasePosition;
    @Autowired
    private PageCollection pageCollection;

    @Autowired
    TokenCollection tokenCollection;

    Ranker(){}
    //opt
    public List<Result> rank(List<String> tokens, int type,String[] op){
        if(tokens.isEmpty()) return null;

        searchTokens = new ArrayList<>();
        pageScore = new HashMap<String,Double>();
        results = new ArrayList<>();
        phrasePosition= new ArrayList<>();
        TYPE=type;

        formTokens(tokens);
        
        if(TYPE==0)
        {
            System.out.println("--------WORD SEARCH--------");
            rankByWord();
        }
        else if(TYPE==1)
        {
            System.out.println("--------Phrase SEARCH--------");
            rankByPhrase();
        }
        else return null;
        return results;
    }
    //opt
    private void formTokens(List<String> tokens)
    {
        searchPhrase=String.join(" ", tokens);
        
        tokens.forEach(token -> {
            Optional<Token> token_ = tokenCollection.findOneByTerm(token);
            if(token_.isPresent())
                searchTokens.add(token_.get());
        });
    }
    //opt
    private Double calculateIDF(String token_){
        
        Optional<Token> token = tokenCollection.findOneByTerm(token_);
        
        if(token.isPresent()){
            return Math.log(pageCollection.count() / token.get().getDocuments().size());
        }

        return 0.0;
    }
    //opt
    private double scoreHtml(String pos){
        double relevance; //not normalized yet
        switch (pos) {
            case "h1":
                relevance = H1_TAG_SCORE;
                break;
            case "h2":
                relevance = H2_TAG_SCORE;
                break;
            case "h3":
                relevance = H3_TAG_SCORE;
                break;
            case "p":
                relevance = P_TAG_SCORE;
                break;
            default:
                relevance = OTHER;
                break;
        }

        return relevance;
    }
    //opt
    private void scorePages(Token token, Double idf){
        token.getDocuments().forEach(document -> {
            Double TF = document.getTF();
            Double score = TF * idf + scoreHtml(document.getHtml_pos());
            String id = document.getDocid();

            if(pageScore.containsKey(id)){
                pageScore.put(id, pageScore.get(id) + score);
            }else{
                pageScore.put(id, score);
            }
        });
    }
    //opt
    private void rankByWord()
    {
        searchTokens.forEach(token -> {
            String tokenTerm=token.getTerm();
            Double idf = calculateIDF(tokenTerm);
            scorePages(token, idf);
        });   
        formResult();
    }
    //opt
    private void rankByPhrase()
    {
        if(searchTokens.isEmpty()) return;
        String path;
        List<String> docsContainingAllTokens=filterDocs();
        for (String string : docsContainingAllTokens) {
            Optional<Page> page = pageCollection.findById(string);
            if(page.isPresent())
            {
                path=page.get().getDoc_path();
                Double score=phraseSearch(path);
                if(score>0)
                    pageScore.put(page.get().getId(), score);
            }   
        }
        formResult();
        return;
    }
    //opt
    private void formResult()
    {
            pageScore.forEach((docid,score) -> {
            
            Optional<Page> page = pageCollection.findById(docid);
            String docPath="";
            String pagePath="";
            if(page.isPresent())
            {
                docPath=page.get().getDoc_path();
                pagePath=page.get().getPath();
            }
            else return;

            Result res = new Result();
            res.setUrl(page.get().getUrl());
            res.setScore(score);
            res.setTitle(getTitle(pagePath));
            
            if(TYPE==0)
                for (Token token : searchTokens) {
                    String term=token.getTerm();
                    String thisSnippet=getSnippet(docPath, term);
                    if(!thisSnippet.isEmpty()){
                        res.setSnippet(thisSnippet);
                        break;
                    }
                }
            else 
            {
                res.setSnippet(getSnippet(docPath, searchPhrase));
            }
            results.add(res);
        });
        results.sort((r1,r2) -> Double.compare(r2.getScore(), r1.getScore()));
    }
    //to be edited
    private String getSnippet(String path, String phrase) {
        String docPath = "E:\\Education\\CMP_SecYear\\SecondSemester\\DuckDiveV01\\IndexerDuck\\".concat(path);
        try {
            File input = new File(docPath);
            Document doc = Jsoup.parse(input, "UTF-8", "");
            Element firstElementContainingWord = doc.select("*:containsOwn(" + phrase + ")").first();
                if (firstElementContainingWord != null) {
                String elementText = firstElementContainingWord.text();
                int phraseIndex = elementText.indexOf(phrase);
                int startCharIndex = Math.max(phraseIndex - 75, 0);
                int endCharIndex = Math.min(phraseIndex + 75, elementText.length() - 1);
                String snippet = elementText.substring(startCharIndex, endCharIndex);
                snippet.concat("...");
                return snippet;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private Double phraseSearch(String path) {
        String docPath = "E:\\Education\\CMP_SecYear\\SecondSemester\\DuckDiveV01\\IndexerDuck\\".concat(path);
        try {
            File input = new File(docPath);
            Document doc = Jsoup.parse(input, "UTF-8", "");
            Element firstElementContainingWord = doc.select("*:containsOwn(" + searchPhrase + ")").first();
                if (firstElementContainingWord != null) {
                    return scoreHtml(firstElementContainingWord.tagName());
                }
            }catch (IOException e) {
                System.err.println("cannot open the file");
                e.printStackTrace();
            }
            return 0.0;
    }
    //to be deleted
    private String getTitle(String pagePath){
        // try {
        //     File input = new File(pagePath);
        //     Document doc = Jsoup.parse(input, "UTF-8", "");
        //     String title=doc.title();
        //     return title;
        // } catch (IOException e) {
        //     System.err.println("cannot retreive the title");
        //     e.printStackTrace();
        // }
        return "";
    }

    private List<String> filterDocs() {
        Set<String> docsContainingAllTokens = new HashSet<>();
        List<Doc> firstTokenDocs = searchTokens.get(0).getDocuments();
        for (Doc doc : firstTokenDocs) {
            docsContainingAllTokens.add(doc.getDocid());
        }
        if(docsContainingAllTokens.isEmpty()) return null;
        for (Token token : searchTokens) {
            List<Doc> thisTokenDocs=token.getDocuments();
            List<String> thisTokenDocs_ids=new ArrayList<>();
            for (Doc doc : thisTokenDocs) {
                thisTokenDocs_ids.add(doc.getDocid());
            }
            docsContainingAllTokens.retainAll(thisTokenDocs_ids);
        }
        if(docsContainingAllTokens.isEmpty()) return null;
        return new ArrayList<>(docsContainingAllTokens);
    }

}

