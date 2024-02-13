package chamara.springdatajpasample.sdjpademo;

import chamara.springdatajpasample.sdjpademo.domain.Book;
import chamara.springdatajpasample.sdjpademo.repositories.BookRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ComponentScan(basePackages = {"chamara.springdatajpasample.sdjpademo.bootstrap"})
public class SpringbootJpaTest {
    @Autowired
    BookRepository bookRepository;

    @Test
    @Order(1)
    @Commit
    void testJPATestSlice() {
        long count = bookRepository.count();

        Book book = new Book("1234", "Spring Framework", "Chamara");

        bookRepository.save(book);

        long countAfterSave = bookRepository.count();

        assertThat(countAfterSave).isGreaterThan(count);

    }

    @Test
    @Order(2)
    void testJPATestSliceTransaction() {
        long count = bookRepository.count();
        assertThat(count).isEqualTo(3);


    }
}
