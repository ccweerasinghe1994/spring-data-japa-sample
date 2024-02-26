package chamara.springdatajpasample.sdjpademo.doa;

import chamara.springdatajpasample.sdjpademo.domain.Author;

public interface AuthorDoa {
    Author getAuthorById(Long id);

    Author findAuthorByFirstName(String firstName);
}
