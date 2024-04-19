package com.indexer.tokenizer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;




// Technical Depts

// 1- tokenization process need to be imporoved (splitting)
// 2- we need to measure speed
// 3- If you could reimplement the parsing process


public class Tokenizer {

    String output_path;
    File output;
    String path;
    HashMap<String,Token> tokenDic;
    Integer counter;
    BufferedWriter w;

    public Tokenizer(String path,String docId)
    {

        String output_path = "/home/shehab/Desktop/DuckDive/IndexerDuck/docs/";
        this.path = path;
        this.output_path = output_path + docId + ".txt";
        this.tokenDic = new HashMap<String,Token>();
        this.counter = 0;

    }

    private void tokenize_helper(String text, String nodeName)
    {
        Pattern pattern = Pattern.compile("\\s+");
        String[] splitTokens = pattern.split(text);

        for (String token : splitTokens) {

            token = token.toLowerCase().replaceAll("[^a-zA-Z ]", "");

            if(token.isEmpty())
                continue;

            try {
                w.write(token + " ");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            this.counter++;

            if(!tokenDic.containsKey(token))
            {
                Token newT = new Token(token,nodeName);
                tokenDic.put(token,newT);
                newT.position.add(counter);
            }
            else{
                Token t = tokenDic.get(token);
                t.TF++;
                t.html_pos = nodeName;
                t.position.add(this.counter);
            }
        }
    }

    private void traverse(Node root)
    {
        if(root instanceof TextNode)
        {
            tokenize_helper(((TextNode) root).text(), Objects.requireNonNull(root.parent()).nodeName());
            return;
        }

        List<Node> nodes = root.childNodes();

        for (Node node : nodes) {
            traverse(node);
        }

    }

    public void tokenize()
    {
        Document doc;

        try {
            File file = new File(this.path);
            doc = Jsoup.parse(file);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try {
            this.w = new BufferedWriter(new FileWriter(output_path)); // Initialize BufferedWriter with FileWriter
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String title = doc.title();
        tokenize_helper(title,"title");

        Element body = doc.body();
        traverse(body);

        try {
            this.w.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public HashMap<String, Token> getTokenizedData() {
        return tokenDic;
    }
}


