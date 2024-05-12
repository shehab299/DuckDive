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
    @Autowired
    private PageCollection pageCollection;

    @Autowired
    TokenCollection tokenCollection;

    Ranker()
    {
        searchTokens = new ArrayList<>();
        pageScore = new HashMap<String,Double>();
        results = new ArrayList<>();
    }
     
    void formPhrase() //
    {
        searchPhrase="";
        int i=0;
        int numOfTokens=searchTokens.size();
        for (Token token : searchTokens) {
            searchPhrase=searchPhrase.concat(token.getTerm());
            if(i++!=numOfTokens) searchPhrase=searchPhrase.concat(" ");
        }
    }

    private void formTokens(List<String> tokens)
    {
        tokens.forEach(token -> {
            Optional<Token> token_ = tokenCollection.findOneByTerm(token);
            if(token_.isPresent())
                searchTokens.add(token_.get());
        });   
    }

    private Double calculateIDF(String token_){
        
        Optional<Token> token = tokenCollection.findOneByTerm(token_);
        
        if(token.isPresent()){
            System.out.println(token.get().getTerm());
            return Math.log(pageCollection.count() / token.get().getDocuments().size());
        }

        return 0.0;
    }

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

    public List<Result> rank(List<String> tokens){
        searchTokens = new ArrayList<>();
        pageScore = new HashMap<String,Double>();
        results = new ArrayList<>();
        TYPE=1;
        formTokens(tokens);
        formPhrase();
        if(tokens.size()==1)
            TYPE=0;
        if(TYPE==0)
            rankByWord();
        else if(TYPE==1)
            rankByPhrase();
        else return null;
        return results;
    }

    void scorePages(Token token, Double idf){
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

    private void rankByWord()
    {
        searchTokens.forEach(token -> {
            String tokenTerm=token.getTerm();
            Double idf = calculateIDF(tokenTerm);
            scorePages(token, idf);
        });   
        formResult();
    }

    private void rankByPhrase()
    {
        String path;
        List<String> docsContainingAllTokens=filterDocs();
        for (String string : docsContainingAllTokens) {
            Optional<Page> page=pageCollection.findById(string);
            if(page.isPresent())
            {
                path=page.get().getDoc_path();
                Double score=scorePages_phraseSearch(path);
                if(score>0)
                    pageScore.put(page.get().getId(), score);
            }   
        }
        formResult();
        return;
    }

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

    private Double scorePages_phraseSearch(String path) {
        System.out.println(searchPhrase);
        String docPath = "E:\\Education\\CMP_SecYear\\SecondSemester\\DuckDiveV01\\IndexerDuck\\".concat(path);
        try {
            File input = new File(docPath);
            Document doc = Jsoup.parse(input, "UTF-8", "");
            Element firstElementContainingWord = doc.select("*:containsOwn(" + searchPhrase + ")").first();
                if (firstElementContainingWord != null) {
                    return 1.0;
                }
            }catch (IOException e) {
                System.err.println("cannot opent the file");
                e.printStackTrace();
            }
            System.out.println("no score");
            return 0.0;
    }

    private String getTitle(String pagePath){
        try {
            System.out.println(pagePath);
            File input = new File(pagePath);
            Document doc = Jsoup.parse(input, "UTF-8", "");
            String title=doc.title();
            if (title.isEmpty()) {
                System.out.println("No title found in the HTML document.");
            } else {
                System.out.println("Title: " + title);
            }
            return title;
        } catch (IOException e) {
            System.err.println("cannot retreive the title");
            e.printStackTrace();
        }
        return "";
    }

    // private List<String> filterDocs() {
    //     List<String> docsContainingPhraseTokens=new ArrayList<>();
    //     List<Doc> firstTokenDoc =searchTokens.get(0).getDocuments();
    //     for (Doc doc :firstTokenDoc)
    //         docsContainingPhraseTokens.add(doc.getDocid());

    //     for (Token token : searchTokens) {
    //         List<String> docIds=new ArrayList<>();
    //         List<Doc> thisTokenDocs=token.getDocuments();
    //         for (Doc doc : thisTokenDocs)
    //             docIds.add(doc.getDocid());
    //         docsContainingPhraseTokens.retainAll(docIds);
    //     }
    //     return docsContainingPhraseTokens;
    // }

    private List<String> filterDocs() {
        Set<String> docsContainingAllTokens = new HashSet<>();
        List<Doc> firstTokenDocs = searchTokens.get(0).getDocuments();
        for (Doc doc : firstTokenDocs) {
            docsContainingAllTokens.add(doc.getDocid());
        }
        
        for (Token token : searchTokens) {
            List<Doc> thisTokenDocs=token.getDocuments();
            List<String> thisTokenDocs_ids=new ArrayList<>();
            for (Doc doc : thisTokenDocs) {
                thisTokenDocs_ids.add(doc.getDocid());
            }
            docsContainingAllTokens.retainAll(thisTokenDocs_ids);
        }
        return new ArrayList<>(docsContainingAllTokens);
    }

//     private List<String> filterDocs() {
//     List<String> docsContainingAllTokens = new ArrayList<>();
//     List<Doc> firstTokenDocs = searchTokens.get(0).getDocuments();
//     for (Doc doc : firstTokenDocs) {
//         docsContainingAllTokens.add(doc.getDocid());
//     }
    
//     for (int i = 1; i < searchTokens.size(); i++) {
//         List<String> docIds = new ArrayList<>();
//         List<Doc> thisTokenDocs = searchTokens.get(i).getDocuments();
//         for (Doc doc : thisTokenDocs) {
//             docIds.add(doc.getDocid());
//         }
//         docsContainingAllTokens.retainAll(docIds);
//     }
//     return docsContainingAllTokens;
//     }
// }
}