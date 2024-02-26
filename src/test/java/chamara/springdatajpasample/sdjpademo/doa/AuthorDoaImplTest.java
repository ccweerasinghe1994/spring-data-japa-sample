package chamara.springdatajpasample.sdjpademo.doa;

import chamara.springdatajpasample.sdjpademo.domain.Author;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("local")
@DataJpaTest
@ComponentScan(basePackages = "chamara.springdatajpasample.sdjpademo.doa")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AuthorDoaImplTest {
    @Autowired
    AuthorDoa authorDoa;

    @Test
    void itShouldReturnAuthorWhenIdIsProvided() {
        Author author = authorDoa.getAuthorById(1L);
        assertThat(author).isNotNull();
    }

    @Test
    void itShouldReturnAuthorWhenFirstNameIsProvided() {
        Author author = authorDoa.findAuthorByFirstName("Craig");
        assertThat(author).isNotNull();
    }
}