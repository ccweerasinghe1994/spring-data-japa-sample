package chamara.springdatajpasample.sdjpademo;


import chamara.springdatajpasample.sdjpademo.domain.BookNatural;
import chamara.springdatajpasample.sdjpademo.repositories.BookNaturalRepository;
import chamara.springdatajpasample.sdjpademo.repositories.BookRepository;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ComponentScan(basePackages = {"chamara.springdatajpasample.sdjpademo.bootstrap"})
@ActiveProfiles("local")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MySqlIT {
    @Autowired
    BookRepository bookRepository;

    @Autowired
    BookNaturalRepository bookNaturalRepository;

    @Test
    void bookNaturalTest() {
        BookNatural bookNatural = new BookNatural();
        bookNatural.setTitle("Spring Framework");

        BookNatural savedBookNatural = bookNaturalRepository.save(bookNatural);
        assertThat(savedBookNatural).isNotNull();
    }

    @Test
    void testJPATestSliceTransaction() {
        long count = bookRepository.count();
        assertThat(count).isEqualTo(2);
    }

}
