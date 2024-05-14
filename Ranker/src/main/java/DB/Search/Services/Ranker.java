package DB.Search.Services;

import DB.Search.Collections.PageCollection;
import DB.Search.Collections.TokenCollection;
import DB.Search.Documents.Doc;
import DB.Search.Documents.Page;
import DB.Search.Documents.Result;
import DB.Search.Documents.Token;
import DB.Search.Utils.Tokenizer;


import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
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
    public static final int SNIPPET_LENGTH = 150;

    @Autowired
    private PageCollection pageCollection;

    @Autowired
    private TokenCollection tokenCollection;

    int pageCount;

    HashMap<String,Boolean> stemmedTokens;

    private List<Token> formTokens(String[] tokens)
    {        
        List<Token> searchTokens = new ArrayList<Token>();

        for (String token : tokens) {
            String stemmed = Tokenizer.processWord(token);

            if(stemmed != "" && !stemmedTokens.containsKey(stemmed)){
                stemmedTokens.put(stemmed, true);
            }

            Optional<Token> tokenOptional = tokenCollection.findOneByTerm(stemmed);
            if(tokenOptional.isPresent())
                searchTokens.add(tokenOptional.get());
        }

        return searchTokens;
    }

    Double calcIDF(int pageCount, Token token){
        return Math.log(pageCount / token.getDocuments().size());
    }

    void scoreDocs(HashMap<String,Doc> pageScore, Token token){

        Double idf = calcIDF((int) pageCount, token);

        token.getDocuments().forEach(document -> {
            Double TF = document.getTF();
            Double score = TF * idf;
            String id = document.getDocid();

            Doc doc = pageScore.get(id);

            if(doc != null){
                doc.setScore(doc.getScore() + score);
            }else{
                document.setScore(score);
                pageScore.put(id, document);
            }
        });

    }

    List<Result> formWordResults(HashMap<String , Doc> pageScore){


        List<Result> results = new ArrayList<>();

        pageScore.forEach((docid, doc) -> {

            Optional<Page> pageDoc = pageCollection.findById(docid);

            if(pageDoc.isEmpty())
                return;

            Page page = pageDoc.get();

            String url = page.getUrl();
            String docPath = page.getDoc_path();
            String title = page.getTitle();
            String snippet = getSnippet(docPath, doc);

            Result res = new Result(url, snippet, title, doc.getScore());
            results.add(res);
        });

        results.sort((r1,r2) -> Double.compare(r2.getScore(), r1.getScore()));

        return results;
    }

    public List<Result> searchByWord(String[] words) {
        stemmedTokens = new HashMap<>();
        pageCount = (int) pageCollection.count();
        List<Token> searchTokens = formTokens(words);
        HashMap<String, Doc> pageScore = new HashMap<String, Doc>();
        searchTokens.forEach(token -> scoreDocs(pageScore, token));
        return formWordResults(pageScore);
    }

    private String getWord(RandomAccessFile rFile) throws IOException {

        StringBuilder wordRead = new StringBuilder();

        char c;
        while ((c = (char) rFile.read()) != ' ' && c != '\n' && c != -1) {
            wordRead.append(c);
        }

        return wordRead.toString().toLowerCase();
    }

    public List<Result> searchByPhrase(String[] words, int size) {

        List<Result> results = new ArrayList<Result>();

        String stemmed = Tokenizer.processWord(words[0]);
        Optional<Token> firstToken = tokenCollection.findOneByTerm(stemmed);

        if (firstToken.isEmpty())
            return results;

        firstToken.get().getDocuments().forEach(document -> {

            List<Integer> positions = document.getPositions();

            String id = document.getDocid();

            Optional<Page> optionalPage = pageCollection.findById(id);

            if(!optionalPage.isPresent())
                return;
            
            Page page = optionalPage.get();
            String docPath = page.getDoc_path();

            boolean phraseFound = true;
            RandomAccessFile randomAccessFile = null;

            try{
                randomAccessFile = new RandomAccessFile(docPath, "r");
            }catch(IOException e){
                System.out.println("Cannot open the file: " + docPath);
            }
            

            for (int pos : positions) {
                
                try{
                    randomAccessFile.seek(pos);
                }catch(IOException e){
                    System.out.println("Cannot seek to position: " + pos);
                }

                phraseFound = true;

                for (int i = 0; i < words.length; i++) {
                    String word = words[i];
                    String toCompare = null;

                    try {
                        toCompare = getWord(randomAccessFile);
                    } catch (Exception e) {
                        System.out.println("Can't read File");
                    }

                    if (!word.equals(toCompare)) {
                        phraseFound = false;
                        break;
                    }

                }

                if(phraseFound) {
                    String url = page.getUrl();
                    String title = page.getTitle();
                    String snippet = getPhraseSnippet(randomAccessFile, pos, size);
                    Double score = 0.0;

                    results.add(new Result(url, snippet, title, score));
                    break;
                }
            }

        });
   

        return results;
    }

    private String getPhraseSnippet(RandomAccessFile file,int pos, int size){

        StringBuilder snippet = new StringBuilder();  
        int snipperPosition = pos;

        try {        
            file.seek(snipperPosition);
        } catch (Exception e) {
            System.out.println("Cannot seek to position: " + pos);
        }


        byte[] bytes = new byte[SNIPPET_LENGTH];
        try {
            file.read(bytes);
        } catch (IOException e) {
            System.out.println("Cannot read file");
            return "";
        }
        
        String text = new String(bytes);

        String highligtedText = "\'" + text.substring(0, size) + "\'" + text.substring(size); 

        
        snippet.append(highligtedText);
        snippet.append("...");

        return snippet.toString();
    }



    String newGetSnippet(String docPath, Doc document){

        StringBuilder snippet = new StringBuilder();
        List<Integer> positions = document.getPositions();
        
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(new File(docPath), "r")) {

            int position = positions.get(0);
            randomAccessFile.seek(position);

            String word = null;
            int length = 50;
            while((word = getWord(randomAccessFile)) != "" && (length != 0)){

                if(stemmedTokens.containsKey(word)){
                    word = "_" + word + "_ ";
                }else{
                    word += " ";
                }

                snippet.append(word);
            }

            snippet.append("...");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return snippet.toString();


    }

    String getSnippet(String docPath , Doc document){

        StringBuilder snippet = new StringBuilder();
        List<Integer> positions = document.getPositions();
        
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(new File(docPath), "r")) {

            for (int i = 0; i < 1; i++) {
                int position = positions.get(i);
                int new_pos = position;
                randomAccessFile.seek(new_pos);
                byte[] bytes = new byte[SNIPPET_LENGTH];
                randomAccessFile.read(bytes);
                String text = new String(bytes);


                StringBuilder highligtedText = new StringBuilder();
                String[] words = Tokenizer.splitText(text);

                for (String foundWord : words) {
 
                    String x = Tokenizer.processWord(foundWord);
                    
                    if(stemmedTokens.containsKey(x) && (Boolean) stemmedTokens.get(x) == true){
                        highligtedText.append("\'" + foundWord + "\' ");
                    }else{
                        highligtedText.append(foundWord + " ");
                    }
                }

                snippet.append(highligtedText.toString());
                snippet.append("...");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return snippet.toString();
    }

}

