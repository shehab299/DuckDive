package com.indexer.tokenizer;

import java.util.LinkedList;
import java.util.List;

public class Doc {

    public String term;
    public int TF;

    public List<Integer> positions;

    public Doc(String term) {
        this.term = term;
        this.TF = 0;
        this.positions = new LinkedList<Integer>();
    }





}
