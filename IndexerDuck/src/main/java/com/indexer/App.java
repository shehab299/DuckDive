package com.indexer;


import com.indexer.tokenizer.Token;
import com.indexer.tokenizer.Tokenizer;

import java.util.List;

public class App
{
    public static void main(String[] args) {

        List<Token> x = Tokenizer.tokenize("/home/shehab/Desktop/DuckDive/IndexerDuck/resources/example.html");

        for (Token token : x) {
            System.out.println(token);
        }


    }
}
