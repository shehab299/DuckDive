package com.indexer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class Language {

    static private HashMap<String,Boolean> stopWords = new HashMap<String,Boolean>();

    static public void readStopWords(String filePath){

        Scanner myReader = null;
        try {
            myReader = new Scanner(new File(filePath));
        } catch (FileNotFoundException e) {
            System.out.println("Can't Find Stop Files");
            return;
        }

        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            stopWords.put(data,true);
        }
    }

    static public Boolean isStop(String word){
        return stopWords.get(word) != null;
    }


}
