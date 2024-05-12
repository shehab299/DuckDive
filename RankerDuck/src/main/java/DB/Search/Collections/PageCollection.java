package DB.Search.Collections;

import DB.Search.Documents.Page;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PageCollection extends MongoRepository<Page, String> {

}
