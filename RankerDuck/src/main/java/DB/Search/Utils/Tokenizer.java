package DB.Search.Utils;

import org.tartarus.snowball.SnowballStemmer;
import org.tartarus.snowball.ext.englishStemmer;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


public class Tokenizer {

    public static String stemToken(String token){
        SnowballStemmer stemmer = new englishStemmer();
        stemmer.setCurrent(token);
        return stemmer.getCurrent();
    }

    public static List<String> tokenize(String text)
    {
        Pattern pattern = Pattern.compile("\\s+");
        String[] splitTokens = pattern.split(text);
        List<String> tokens = new ArrayList<String>();

        for (String token : splitTokens) {

            token = token.toLowerCase().replaceAll("[^a-zA-Z ]", "");

            if(Language.isStop(token) || token.isEmpty())
                continue;

            tokens.add(token);
        }

        return tokens;
    }

}







