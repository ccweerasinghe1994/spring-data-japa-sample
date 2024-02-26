package chamara.springdatajpasample.sdjpademo;


import chamara.springdatajpasample.sdjpademo.domain.BookNatural;
import chamara.springdatajpasample.sdjpademo.domain.composite.AuthorComposite;
import chamara.springdatajpasample.sdjpademo.domain.composite.AuthorEmbedded;
import chamara.springdatajpasample.sdjpademo.domain.composite.NameId;
import chamara.springdatajpasample.sdjpademo.repositories.AuthCompositeRepository;
import chamara.springdatajpasample.sdjpademo.repositories.AuthEmbeddedCompositeRepository;
import chamara.springdatajpasample.sdjpademo.repositories.BookNaturalRepository;
import chamara.springdatajpasample.sdjpademo.repositories.BookRepository;
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

    @Autowired
    AuthCompositeRepository authCompositeRepository;

    @Autowired
    AuthEmbeddedCompositeRepository authEmbeddedCompositeRepository;

    @Test
    void setAuthEmbeddedCompositeRepositoryTest() {
        NameId nameId = new NameId("Chamara", "Sumanapala");
        AuthorEmbedded authorEmbedded = new AuthorEmbedded(nameId);

        AuthorEmbedded savedAuthorEmbedded = authEmbeddedCompositeRepository.save(authorEmbedded);
        assertThat(savedAuthorEmbedded).isNotNull();
    }

    @Test
    void setAuthCompositeRepositoryTest() {
        NameId nameId = new NameId("Chamara", "Sumanapala");
        AuthorComposite authorComposite = new AuthorComposite();
        authorComposite.setFirstName(nameId.getFirstName());
        authorComposite.setLastName(nameId.getLastName());

        AuthorComposite savedAuthorComposite = authCompositeRepository.save(authorComposite);
        assertThat(savedAuthorComposite).isNotNull();
    }

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
