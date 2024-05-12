package DB.Search.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class Language {

    static private final HashMap<String,Boolean> stopWords = new HashMap<String,Boolean>();

    @SuppressWarnings("resource")
    static public void Initialize(String filePath){

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
        return stopWords.get(word) != null || word.length() == 1;
    }


}
