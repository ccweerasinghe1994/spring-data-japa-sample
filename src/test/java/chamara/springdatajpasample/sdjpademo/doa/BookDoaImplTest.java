package chamara.springdatajpasample.sdjpademo.doa;

import chamara.springdatajpasample.sdjpademo.repositories.BookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("local")
@DataJpaTest
@ComponentScan(basePackages = "chamara.springdatajpasample.sdjpademo.doa")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookDoaImplTest {
    @Autowired
    BookRepository bookRepository;

    @Test
    void testBookStream() {
        // given
        AtomicInteger count = new AtomicInteger();
        bookRepository.findAllByTitleNotNull().forEach(book -> {
            count.incrementAndGet();
        });
        // when
        // then
        assertThat(count.get()).isGreaterThan(2);
    }

    @Test
    void testEmptyResultException() {
        assertThrows(EmptyResultDataAccessException.class, () -> {
            bookRepository.readByTitle("foobar4");
        });
    }

    @Test
    void testNullParam() {
        assertNull(bookRepository.getByTitle(null));
    }

    @Test
    void testNoException() {
        assertNull(bookRepository.getByTitle("foo"));
    }
}