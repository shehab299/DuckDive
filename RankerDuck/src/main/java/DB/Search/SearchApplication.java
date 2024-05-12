package DB.Search;

import DB.Search.Utils.Language;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.nio.file.Paths;

@SpringBootApplication
public class SearchApplication {
	public static void main(String[] args) {

		String currentDirectory = System.getProperty("user.dir");
		String stopWordsPath = String.valueOf(Paths.get(currentDirectory,"Resources","stops.txt"));
		
		Language.Initialize(stopWordsPath);

		SpringApplication.run(SearchApplication.class, args);
	}
}