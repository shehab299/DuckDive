package DB.Search.Utils;

import org.tartarus.snowball.SnowballStemmer;
import org.tartarus.snowball.ext.englishStemmer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;


public class Tokenizer {

    public static String stemToken(String token){
        SnowballStemmer stemmer = new englishStemmer();
        stemmer.setCurrent(token);
        stemmer.stem();
        return stemmer.getCurrent();
    }

    public static String[] splitText(String text){

        Pattern pattern = Pattern.compile("\\s+");
        return pattern.split(text);
    }

    public static String processWord(String text){

        text = text.toLowerCase().replaceAll("[^a-zA-Z ]", "");

        if(Language.isStop(text) || text.isEmpty())
            return "";

        return stemToken(text);
    }

    public static List<String> tokenize(String text)
    {

        HashMap <String, Boolean> tokenDic = new HashMap<String, Boolean>();

        String[] splitTokens = splitText(text);
        List<String> tokens = new ArrayList<String>();

        for (String token : splitTokens) {

            token = token.toLowerCase().replaceAll("[^a-zA-Z ]", "");

            if(Language.isStop(token) || token.isEmpty())
                continue;

            if(!tokenDic.containsKey(token)){
                tokens.add(token);
                tokenDic.put(token, true);
            }
        }

        return tokens;
    }

}







