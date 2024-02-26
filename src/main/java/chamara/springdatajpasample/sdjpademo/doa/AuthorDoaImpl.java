package chamara.springdatajpasample.sdjpademo.doa;

import chamara.springdatajpasample.sdjpademo.domain.Author;
import org.springframework.stereotype.Component;

@Component
public class AuthorDoaImpl implements AuthorDoa {
    @Override
    public Author getAuthorById(Long id) {
        return null;
    }
}
