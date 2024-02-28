package chamara.springdatajpasample.sdjpademo.repositories;

import chamara.springdatajpasample.sdjpademo.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.Nullable;

import java.util.Optional;
import java.util.stream.Stream;

public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findBookByTitle(String title);

    Book readByTitle(String title);

    @Nullable
    Book getByTitle(@Nullable String title);

    Stream<Book> findAllByTitleNotNull();
}
