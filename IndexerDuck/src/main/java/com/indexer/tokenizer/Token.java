package com.indexer.tokenizer;

public class Token {
    public String token_text;
    public String html_pos;
    public int position;

    @Override
    public String toString() {
        return "{" +
                "token_text=" + token_text +
                ", html_pos=" + html_pos +
                ", position=" + position +
                '}';
    }

    public Token(String token_text, String html_pos, int position) {
        this.token_text = token_text;
        this.html_pos = html_pos;
        this.position = position;
    }

}
