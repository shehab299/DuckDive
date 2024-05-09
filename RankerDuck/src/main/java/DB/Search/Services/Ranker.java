package DB.Search.Services;

import DB.Search.Collections.PageCollection;
import DB.Search.Documents.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class Ranker {
    public static final double H1_TAG_SCORE = 0.5;
    public static final double H2_TAG_SCORE = 0.4;
    public static final double H3_TAG_SCORE = 0.3;
    public static final double P_TAG_SCORE = 0.2;
    public static final double OTHER = 0.1;
    public static final int SNIPPET_LENGTH = 150;
    public static int TYPE;
    private int collectionSize;

    private List<Token> searchTokens;
    private Map<String, Double> documentScores;
    private MyPair[] sortedDocumentScores;
    private Map<String, List<String>> tokenMap; // term docid
    private static String tokenPhrase;
    QueryResult finalResult;

    @Autowired
    private PageCollection pageCollection;

    @Autowired
    Ranker()
    {
        searchTokens = new ArrayList<>();
        documentScores = new TreeMap<>();
        finalResult=new QueryResult(null);
    }

    public QueryResult rank(int type, List<Token> tokens,int collectionSize) //OK
    {
        this.collectionSize = collectionSize;
        searchTokens = tokens;
        TYPE = type;
        if (type == 0)
            rankByWord();
        else if (type == 1)
            rankByPhrase();

        sortByScore();
        formResult();
        return finalResult;
    }

    private void rankByWord()   //OK
    {
        for (Token token : searchTokens)
            giveScores(token.getDocuments());
    }

    private boolean rankByPhrase() {
        formMap();
        Set<String> intersectIds = getDocsIntersection();
        if (intersectIds.isEmpty()) return false;
        filterDocs(intersectIds);
        return true;
    }

    private void filterDocs(Set<String> intersectIds) {
        List<Doc> allPossibleTokenDocs=searchTokens.get(0).getDocuments();
        for(String intersectid: intersectIds)
        {
            for(Doc possibleDoc: allPossibleTokenDocs)
            {
                if(intersectid.equals(possibleDoc.getDocid()))
                {
                    Optional<Page> page = pageCollection.findById(possibleDoc.getDocid());
                    if(!page.isPresent()) continue;
                    String path=page.get().getDoc_path();
                    double numOfOccur=containsPhrase(path);
                    if(numOfOccur>0)
                    {
                        documentScores.put(possibleDoc.getDocid(),numOfOccur);
                    }
                }
            }
        }
        sortByScore();
    }

    private double containsPhrase(String path) {
        try {
            String htmlContent = String.join("\n", Files.readAllLines(Paths.get(path))).toLowerCase();
            if(!htmlContent.contains(tokenPhrase))
                return -1;
            return countPhraseOccurrences(htmlContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static double countPhraseOccurrences(String string) {        //Check
        String stringLower = string.toLowerCase();
        String phraseLower = tokenPhrase.toLowerCase();
        String[] substrings = stringLower.split("\\s+");                ///CHECKKCKKCCKK
        int count = 0;
        for (int i = 0; i < substrings.length - 1; i++) {
            String currentSubstring = substrings[i];
            String nextSubstring = substrings[i + 1];
            String potentialPhrase = currentSubstring + " " + nextSubstring;
            if (potentialPhrase.equals(phraseLower)) {
                count++;
            }
        }
        return count;
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

    private boolean giveScores(List<Doc> documents)    //OK
    {
        if(documents.isEmpty()) return false;

        Double IDF = Math.log((double) documents.size() / collectionSize);

        for (Doc doc : documents) {

            double relevance = scoreHtml(doc.getHtml_pos());
            Double score = doc.getTF() * IDF + relevance;

            if (documentScores.containsKey(doc.getDocid()))
                documentScores.put(doc.getDocid(), documentScores.get(doc.getDocid()) + score);
            else
                documentScores.put(doc.getDocid(), score);
        }

        return true;
    }

    private boolean sortByScore() //OK
    {
        if(documentScores.isEmpty()) return false;
        int index = 0;
        sortedDocumentScores = new MyPair[documentScores.size()];
        for (Map.Entry<String, Double> entry : documentScores.entrySet()) {
            sortedDocumentScores[index] = new MyPair(entry.getKey(), -entry.getValue());
            index++;
        }
        Arrays.sort(sortedDocumentScores, Comparator.comparing(MyPair::getValue));
        return true;
    }

    String formPhrase() //OK
    {
        String[] phrase = new String[150];
        int i = 0;
        for (Token token : searchTokens) {
            phrase[i++] = token.getTerm();
        }
        tokenPhrase=String.join(" ", phrase);
        return tokenPhrase;
    }

    private void formResult()   //OK
    {
        if(sortedDocumentScores==null) return;
        List<String> docsPath = new ArrayList<>();
        List<String> urls = new ArrayList<>();
        List<String> snippets = new ArrayList<>();
        //List<String> titles = new ArrayList<>();    //needs to be included in the db

        for (MyPair doc : sortedDocumentScores) {
            Optional<Page> page = pageCollection.findById(doc.getKey());
            if (page.isPresent()) {
                String path = page.get().getPath();
                docsPath.add(path);
                urls.add(page.get().getUrl());
                if(TYPE==0) {
                    for (Token token : searchTokens) {
                        String thisTokenSnippet=getSnippet(path, token.getTerm());
                        if (!thisTokenSnippet.isEmpty()) {snippets.add(thisTokenSnippet); break;}
                    }
                }
                else if (TYPE==1) {
                    String phrase=formPhrase();
                    if (!phrase.isEmpty()) {
                        String phraseSnippet=getSnippet(path, phrase);
                        if(!phraseSnippet.isEmpty())
                            snippets.add(phraseSnippet);
                        }
                }
            }
        }

        finalResult.setDocPath(docsPath);
        finalResult.setUrls(urls);
        // finalResult.setSnippet(snippets);
        //finalResult.setTitles(titles);
    }

    public String getSnippet(String docPath, String phrase) //OK
    {
        String snippet = "";
        try {
            String htmlContent = String.join("\n", Files.readAllLines(Paths.get(docPath)));
            snippet = htmlContent.substring(htmlContent.indexOf(phrase), SNIPPET_LENGTH - phrase.length());
            return snippet;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return snippet;
    }

    void formMap()  //OK
    {
        for (Token token : searchTokens) {
            List<String> thisTokenDocIds = new ArrayList<>();
            String term = token.getTerm();
            List<Doc> thisTokenDocs = token.getDocuments();
            for (Doc doc : thisTokenDocs)
                thisTokenDocIds.add(doc.getDocid());
            tokenMap.put(term, thisTokenDocIds); //term -> all docs ids
        }
    }

    Set<String> getDocsIntersection() //OK
    {
        Set<String> matchingDocumentsSet = new HashSet<>();
        List<Doc> firstTokenDocs=searchTokens.get(0).getDocuments();
        for (Doc doc : firstTokenDocs)
            matchingDocumentsSet.add(doc.getDocid());

        for (int i = 1; i < searchTokens.size(); i++) {
            String term = searchTokens.get(i).getTerm();
            List<String> docIds = tokenMap.get(term);
            matchingDocumentsSet.retainAll(docIds);
        }
        return matchingDocumentsSet;
    }

    // public static void main(String[] args) {
    //     System.err.println("ay 7aga");
    }


