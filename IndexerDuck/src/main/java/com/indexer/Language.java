package com.indexer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;


public class Language {

    static private HashMap<String,Boolean> stopWords = new HashMap<String,Boolean>();

    static public HashMap<String,Integer> scoreDictionary = new HashMap<String,Integer>(); 

    static public int scoreHtml(String pos){
        
        if(scoreDictionary.get(pos) == null){
            return 1;
        }

        return scoreDictionary.get(pos);
    }

    static public void initalizeHTML(String filePath){

        Scanner myReader = null;
        try {
            myReader = new Scanner(new File(filePath));
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] score_entry = data.split(" ");

                String pos = score_entry[0];
                Integer score = Integer.parseInt(score_entry[1]);

                scoreDictionary.put(pos,score);
            }

        } catch (FileNotFoundException e) {
            System.out.println("Can't Find HTML POS Files");
            return;
        } finally {
            if (myReader != null) {
                myReader.close();
            }
        }        


    }
    
    static public void initalizeDictionary(String filePath){

        Scanner myReader = null;
        try {
            myReader = new Scanner(new File(filePath));
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                stopWords.put(data,true);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Can't Find Stop Files");
            return;
        } finally {
            if (myReader != null) {
                myReader.close();
            }
        }
    }

    static public Boolean isStop(String word){
        return stopWords.get(word) != null || word.length() < 2;
    }

}
