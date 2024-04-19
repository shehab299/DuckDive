package com.indexer.tokenizer;
import java.util.ArrayList;

public class Token {
    public String term;
    public String html_pos;
    public int TF;
    public ArrayList<Integer> position; // Changed to ArrayList of integers

    @Override
    public String toString() {
        return "{" +
                "term=" + term +
                ", html_pos=" + html_pos +
                ", position=" + position +
                '}';
    }

    public Token(String term, String html_pos) { // Changed parameter type
        this.term = term;
        this.html_pos = html_pos;
        this.position = new ArrayList<>();
        this.TF = 0;
    }
}
