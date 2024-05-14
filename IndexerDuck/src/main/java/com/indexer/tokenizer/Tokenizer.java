package com.indexer.tokenizer;

import com.indexer.Language;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.tartarus.snowball.SnowballStemmer;
import org.tartarus.snowball.ext.englishStemmer;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class Tokenizer {

    private Document doc;
    private int wordCount;
    private HashMap<String, Token> tokenDic;
    private RandomAccessFile file;
    private int cursor;

    public Tokenizer(String path) {
        this.cursor = 1;

        try {
            File inputFile = new File(path);
            doc = Jsoup.parse(inputFile, "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException("Can't Parse File", e);
        }
    }

    private int getFilePosition() {
        try {
            return (int) file.getFilePointer();
        } catch (IOException e) {
            throw new RuntimeException("Error getting file position", e);
        }
    }

    private String removeSpecialChars(String token) {
        return token.toLowerCase().replaceAll("[^a-zA-Z ]", "");
    }

    private void writeToFile(String token) {
        try {
            file.writeBytes(token + " ");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.cursor += token.length() + 1;
    }

    private String compare(String pos1, String pos2) {
        int score1 = Language.scoreHtml(pos1);
        int score2 = Language.scoreHtml(pos2);

        return score1 >= score2 ? pos1 : pos2;
    }

    private void tokenize(String text, String nodeName) {
        Pattern pattern = Pattern.compile("\\s+");
        String[] splitTokens = pattern.split(text);

        for (String token : splitTokens) {
            int position = getFilePosition();
            writeToFile(token);

            token = removeSpecialChars(token);

            if (Language.isStop(token))
                continue;

            token = stemToken(token);

            if (!tokenDic.containsKey(token)) {
                Token newT = new Token(token, nodeName);
                tokenDic.put(token, newT);
                newT.position.add(position);
            } else {
                Token t = tokenDic.get(token);
                t.TF++;
                t.html_pos = compare(t.html_pos, nodeName);
                t.position.add(position);
            }

            wordCount++;
        }
    }

    public int getWordCount() {
        return wordCount;
    }

    private void tokenizeDOM(Node root) {
        if (root instanceof TextNode) {
            tokenize(((TextNode) root).text(), Objects.requireNonNull(root.parent()).nodeName());
            return;
        }

        List<Node> nodes = root.childNodes();
        for (Node node : nodes) {
            tokenizeDOM(node);
        }
    }

    public HashMap<String, Token> tokenizeDocument(String docPath) {
        tokenDic = new HashMap<>();

        openFile(docPath);

        String title = doc.title();
        tokenize(title, "title");

        Element body = doc.body();
        tokenizeDOM(body);

        closeFile();

        return tokenDic;
    }

    private void closeFile() {
        try {
            this.file.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void openFile(String path) {
        try {
            file = new RandomAccessFile(path, "rw");
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Can't open file", e);
        }
    }

    private String stemToken(String token) {
        SnowballStemmer stemmer = new englishStemmer();
        stemmer.setCurrent(token);
        stemmer.stem();
        return stemmer.getCurrent();
    }
}
