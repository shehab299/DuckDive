package DB.Search.Services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import DB.Search.Collections.PageCollection;
import DB.Search.Documents.Doc;
import DB.Search.Documents.Page;
import DB.Search.Documents.Token;

public class Ranker {
    public Map<String,Double> documentsIdsList;
    List<Token> searchTokens;
    Long collectionSize;
    public static final double H1_TAG_SCORE = 0.9; 
    public static final double H2_TAG_SCORE = 0.7;
    public static final double H3_TAG_SCORE = 0.5;
    public static final double P_TAG_SCORE = 0.45;
    public static final double OTHER = 0.1;

    @Autowired
    PageCollection pageCollection;

    Ranker(List<Token> tokens, Long size)
    {   
        documentsIdsList = new TreeMap<String,Double>(Collections.reverseOrder());
        this.collectionSize=size;
        searchTokens=tokens;
    }

    List<String> rank()
    {
        for (Token token : searchTokens)
            giveScores(token.getDocuments());

       return new ArrayList<>(documentsIdsList.keySet());
    }

    void giveScores(List<Doc> docList)
    {
        Double IDF = Math.log(docList.size() / collectionSize);
        for (Doc doc : docList) {
            double relevance; //do not know whether to normalize it/how to, yet
            switch (doc.getHtml_pos()) {    //getHtml_pos() won't work for the phrase searching algorithm
                case "h1": relevance=H1_TAG_SCORE;
                    break;
                case "h2": relevance=H2_TAG_SCORE;
                    break;
                case "h3": relevance=H3_TAG_SCORE;
                    break;
                case "p":  relevance=P_TAG_SCORE;
                    break;
                default:   relevance=OTHER;
                    break;
            }
            Double score = doc.getTF() * IDF + relevance;
            if (documentsIdsList.containsKey(doc.getDocid()))
                documentsIdsList.put(doc.getDocid(), documentsIdsList.get(doc.getDocid()) + score);
            else 
                documentsIdsList.put(doc.getDocid(), score);
        }
    }

    void getPagesData(List<String> pagesUrl,List<String> pagesPath, List<String> docsPath)
    {
        for (String id: documentsIdsList.keySet()) {
            Page page=pageCollection.findById(id);
            pagesUrl.add(page.getUrl());
            pagesPath.add(page.getPath());
            docsPath.add(page.getDoc_path());
        }
    }
    
}
