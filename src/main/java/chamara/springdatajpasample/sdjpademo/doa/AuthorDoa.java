package chamara.springdatajpasample.sdjpademo.doa;

import chamara.springdatajpasample.sdjpademo.domain.Author;

public interface AuthorDoa {
    Author getAuthorById(Long id);

    Author findAuthorByFirstName(String firstName);

    Author saveAuthor(Author author);

    Author updateAuthor(Author author);

    void deleteAuthor(Long id);
}
