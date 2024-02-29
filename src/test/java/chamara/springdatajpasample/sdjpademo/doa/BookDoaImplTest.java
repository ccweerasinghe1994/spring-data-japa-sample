package chamara.springdatajpasample.sdjpademo.doa;

import chamara.springdatajpasample.sdjpademo.domain.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("local")
@DataJpaTest
@ComponentScan(basePackages = "chamara.springdatajpasample.sdjpademo.doa")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookDoaImplTest {
    @Autowired
    JdbcTemplate jdbcTemplate;

    BookDoa bookDao;

    @BeforeEach
    void setUp() {
        bookDao = new BookDoaJDBCTemplate(jdbcTemplate);
    }

    @Test
    void findAllBooksPageableSorted() {
        // given
        Sort title = Sort.by(Sort.Order.asc("title"));
        List<Book> books = bookDao.findAllBooksByTitleSorted(PageRequest.of(0, 2, title));
        // when
        // then
        assertThat(books).isNotNull();
        assertThat(books.size()).isEqualTo(2);
    }

    @Test
    void findAllBooksPageable1() {
        // given
        List<Book> books = bookDao.findAllBooks(PageRequest.of(0, 2));
        // when
        // then
        assertThat(books).isNotNull();
        assertThat(books.size()).isEqualTo(2);
    }

    @Test
    void findAllBooksPageable2() {
        // given
        List<Book> books = bookDao.findAllBooks(PageRequest.of(1, 2));
        // when
        // then
        assertThat(books).isNotNull();
        assertThat(books.size()).isEqualTo(2);
    }

    @Test
    void findAllBooksPageable10() {
        // given
        List<Book> books = bookDao.findAllBooks(PageRequest.of(10, 2));
        // when
        // then
        assertThat(books).isNotNull();
        assertThat(books.size()).isEqualTo(0);
    }

    @Test
    void findAllBooksInPage1() {
        // given
        List<Book> books = bookDao.findAllBooks(2, 0);
        // when
        // then
        assertThat(books).isNotNull();
        assertThat(books.size()).isEqualTo(2);
    }

    @Test
    void findAllBooksInPage2() {
        // given
        List<Book> books = bookDao.findAllBooks(2, 2);
        // when
        // then
        assertThat(books).isNotNull();
        assertThat(books.size()).isEqualTo(2);
    }

    @Test
    void findAllBooksInPage100() {
        // given
        List<Book> books = bookDao.findAllBooks(2, 200);
        // when
        // then
        assertThat(books).isNotNull();
        assertThat(books.size()).isEqualTo(0);
    }


    @Test
    void testFindAllBooks() {
        List<Book> books = bookDao.findAllBooks();

        assertThat(books).isNotNull();
        assertThat(books.size()).isGreaterThan(2);
    }

    @Test
    void getById() {
        Book book = bookDao.getById(3L);

        assertThat(book.getId()).isNotNull();
    }

    @Test
    void findBookByTitle() {
        Book book = bookDao.findBookByTitle("Clean Code");

        assertThat(book).isNotNull();
    }

    @Test
    void saveNewBook() {
        Book book = new Book();
        book.setIsbn("1234");
        book.setPublisher("Self");
        book.setTitle("my book");
        book.setAuthorId(1L);

        Book saved = bookDao.saveNewBook(book);

        assertThat(saved).isNotNull();
    }

    @Test
    void updateBook() {
        Book book = new Book();
        book.setIsbn("1234");
        book.setPublisher("Self");
        book.setTitle("my book");
        book.setAuthorId(1L);
        Book saved = bookDao.saveNewBook(book);

        saved.setTitle("New Book");
        bookDao.updateBook(saved);

        Book fetched = bookDao.getById(saved.getId());

        assertThat(fetched.getTitle()).isEqualTo("New Book");
    }

    @Test
    void deleteBookById() {

        Book book = new Book();
        book.setIsbn("1234");
        book.setPublisher("Self");
        book.setTitle("my book");
        Book saved = bookDao.saveNewBook(book);

        bookDao.deleteBookById(saved.getId());

        assertThrows(EmptyResultDataAccessException.class, () -> {
            bookDao.getById(saved.getId());
        });
    }
}