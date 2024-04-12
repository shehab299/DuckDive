package com.indexer.tokenizer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;


// Technical Depts

// 1- tokenization process need to be imporoved (splitting)
// 2- we need to measure speed
// 3- If you could reimplement the parsing process


public class Tokenizer {

    private static void tokenize_helper(String text,String nodeName ,List<Token> tokens){

            Pattern pattern = Pattern.compile("\\s+");
            String[] splitTokens = pattern.split(text);

            for (String token : splitTokens) {

                token = token.toLowerCase().replaceAll("[^a-zA-Z ]", "");

                if(!token.isEmpty())
                {
                    Token newToken = new Token(token,nodeName,0);
                    tokens.add(newToken);
                }
            }
    }

    private static void traverse(Node root, List<Token> tokens){

        if(root instanceof TextNode)
        {
            tokenize_helper(((TextNode) root).text(), root.parent().nodeName(),tokens);
            return;
        }

        List<Node> nodes = root.childNodes();
        for (Node node : nodes) {
            traverse(node,tokens);
        }
    }

    public static List<Token> tokenize(String path){

        List<Token> tokens = new LinkedList<>();
        Document doc;

        try {
            File file = new File(path);
            doc = Jsoup.parse(file);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        String title = doc.title();
        tokenize_helper(title,"title",tokens);

        Element body = doc.body();
        traverse(body,tokens);

        return tokens;
    }

}


