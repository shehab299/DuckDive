package main.java.DB.Search.Services;
    
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import DB.Search.Collections.PageCollection;
import DB.Search.Documents.*;
import DB.Search.Services.Autowired;

public class PhraseRanker {

    private Map<String,Double> documentScore; // id key
    private Map<String,List<String>> tokenMap; // term key
    private Map<Integer,List<Integer>>[] positionMapArray;
    private int phraseNumOfDocs;
    private List<Token> searchTokens;
    private Long collectionSize;
    public static final double H1_TAG_SCORE = 0.9; 
    public static final double H2_TAG_SCORE = 0.7;
    public static final double H3_TAG_SCORE = 0.5;
    public static final double P_TAG_SCORE = 0.45;
    public static final double OTHER = 0.1;
    public static boolean TYPE= true; // true =Search by Word, false="Whole Sentence"

    @Autowired
    PageCollection pageCollection;

    //
    PhraseRanker()
    {   
        phraseNumOfDocs=0;
        documentScore = new HashMap<String,Double>();
        positionMapArray=new HashMap[100];
        for(int i=0;i<100;i++)
            positionMapArray[i]=new HashMap<>();
    }
    
    void scoreByWord()
    {
        for (Token token : searchTokens)
            giveScores(token.getDocuments());
    }

    void scoreByPhrase()
    {
        formMap();
        Set<String> intersectIds= getDocsIntersection();
        if(intersectIds == null)
            {documentScore.clear(); return;}
        removeDuplicates(intersectIds);
        filterDocs(intersectIds);
    }
    //
    void formMap()
    {
        for (Token token : searchTokens) {
            List<String> thisTokenDocIds=new ArrayList<>();
            String term=token.getTerm();
            for (Doc doc : token.getDocuments())
                thisTokenDocIds.add(doc.getDocid());
            tokenMap.put(term,thisTokenDocIds);
        }
    }
    
    Set<String> getDocsIntersection()
    {
        Set<String> matchingDocumentsSet = new HashSet<>();
       // List<Integer> [] positionList;
       //initialization
        for (String docId : tokenMap.get(searchTokens.get(0))) { //
            matchingDocumentsSet.add(docId);
        }
        
        boolean notEmpty=removeDuplicates(matchingDocumentsSet);
            if(!notEmpty)
                return null;
        return matchingDocumentsSet;
    }
    //
    boolean removeDuplicates( Set<String> matchingDocumentsSet)
    {
        for (Token token : searchTokens) {
            matchingDocumentsSet.retainAll(tokenMap.get(token.getTerm()));
            if(matchingDocumentsSet.isEmpty())
                return false;
        }
        return true;
    }
    //
    void filterDocs(Set<String> matchingDocumentsSet)
    {
        int tokenInd=-1;
        for (Token token : searchTokens)
        {
            tokenInd++;
            List<Doc> thisTokenDocs=new ArrayList<>();
            thisTokenDocs=token.getDocuments();
            for (Doc doc : thisTokenDocs) {
                if(matchingDocumentsSet.contains(doc.getDocid()));
                {
                    phraseNumOfDocs++;
                    savePositions(doc,tokenInd);
                }
            }
        }
    }     
    
    void savePositions(Doc doc, int tokenInd)
    {
        positionMapArray[tokenInd].put(tokenInd, doc.getPositions());
    }

    boolean canFormPhrase(String docId)
    {
        positionMapArray.

        return false;
    }
    //
    void giveScores(List<Doc> docList) //getHtml_pos() won't work for the phrase searching algorithm
    {
        Double IDF = Math.log(docList.size() / collectionSize);
        for (Doc doc : docList) {
            double relevance; //not normalized yet
            switch (doc.getHtml_pos()) {
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
            if (documentScore.containsKey(doc.getDocid()))
                documentScore.put(doc.getDocid(), documentScore.get(doc.getDocid()) + score);
            else 
                documentScore.put(doc.getDocid(), score);
        }
    }
    

}

/*
 *    //
    // QueryResult getSearchResult()
    // {   
    //     List<String> pagesUrl;
    //     List<String> pagesPath;
    //     List<String> docsPath;
    //     for (String id: documentScore.keySet()) {
    //         Page page=pageCollection.findById(id);
    //         pagesUrl.add(page.getUrl());
    //         pagesPath.add(page.getPath());
    //         docsPath.add(page.getDoc_path());
    //     }
    //     result.setDocPath(docsPath);
    //     result.setPagePath(pagesPath);
    //     result.setUrls(pagesUrl);
    //     return result;
    // }

 */

/* QueryResult Rank(List<Token> tokens, Long size, boolean type)
    {
        this.collectionSize=size;
        searchTokens=tokens;
        
        if(type)
            scoreByWord();
        else scoreByPhrase();

        return getSearchResult();
    } */

/*
        int numOfWords=searchTokens.size();
        int phraseLength=numOfWords-1; //spaces
        List<Integer> termLenght;
        for (Token token : searchTokens) {
            int thisTermLenght=(token.getTerm()).length();
            termLenght.add(thisTermLenght);
            phraseLength+=thisTermLenght;
        }
        String Phrase = formPhrase();
        matchString((searchTokens.get(0)).getDocuments(), termLenght);
 */

 /*
  *     String formPhrase()
    {
        String[] phrase = new String[150];
        int i=0;
        for (Token token : searchTokens) {
            phrase[i++] = token.getTerm();
        }
        return String.join(" ", phrase);
    }

  */