package DB.Search.Collections;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;
import DB.Search.Documents.MyQuery;


public interface QueryCollection extends MongoRepository<MyQuery, String>{

    Optional<MyQuery> findOneByQuery(String query);

    @Query("{'query': {$regex : ?0}}")
    List<MyQuery> findQueryByRegex(String regex);    
}
