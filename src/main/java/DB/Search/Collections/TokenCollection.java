package DB.Search.Collections;


import DB.Search.Documents.Token;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TokenCollection extends MongoRepository<Token, String> {

    Optional<Token> findOneByTerm(String term);
}
