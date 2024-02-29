## 110 - Introduction

![img.png](img.png)

## 111 - Overview of Paging and Sorting

![img_1.png](img_1.png)
![img_2.png](img_2.png)
![img_3.png](img_3.png)
![img_4.png](img_4.png)

## 112 - JDBCTemplate Code Review

## 113 - Find All Books with JDBCTemplate

```java
package chamara.springdatajpasample.sdjpademo.doa;

import chamara.springdatajpasample.sdjpademo.domain.Book;

import java.util.List;

public interface BookDoa {
    List<Book> findAllBooks();

    Book getById(Long id);

    Book findBookByTitle(String title);

    Book saveNewBook(Book book);

    Book updateBook(Book book);

    void deleteBookById(Long id);
}

```

```java
package chamara.springdatajpasample.sdjpademo.doa;

import chamara.springdatajpasample.sdjpademo.domain.Book;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class BookDoaJDBCTemplate implements BookDoa {
    private final JdbcTemplate jdbcTemplate;

    public BookDoaJDBCTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Book> findAllBooks() {
        return jdbcTemplate.query("SELECT * FROM book", getBookMapper());
    }

    @Override
    public Book getById(Long id) {
        return jdbcTemplate.queryForObject("SELECT * FROM book where id = ?", getBookMapper(), id);
    }

    @Override
    public Book findBookByTitle(String title) {
        return jdbcTemplate.queryForObject("SELECT * FROM book where title = ?", getBookMapper(), title);
    }

    @Override
    public Book saveNewBook(Book book) {
        jdbcTemplate.update("INSERT INTO book (isbn, publisher, title, author_id) VALUES (?, ?, ?, ?)",
                book.getIsbn(), book.getPublisher(), book.getTitle(), book.getAuthorId());

        Long createdId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);

        return this.getById(createdId);
    }

    @Override
    public Book updateBook(Book book) {
        jdbcTemplate.update("UPDATE book set isbn = ?, publisher = ?, title = ?, author_id = ? where id = ?",
                book.getIsbn(), book.getPublisher(), book.getTitle(), book.getAuthorId(), book.getId());

        return this.getById(book.getId());
    }

    @Override
    public void deleteBookById(Long id) {
        jdbcTemplate.update("DELETE from book where id = ?", id);
    }

    private BookMapper getBookMapper() {
        return new BookMapper();
    }
}

```

```java

@Test
void testFindAllBooks() {
    List<Book> books = bookDao.findAllBooks();

    assertThat(books).isNotNull();
    assertThat(books.size()).isGreaterThan(2);
}
```

## 114 - Find All Books with Paging

```java
package chamara.springdatajpasample.sdjpademo.doa;

import chamara.springdatajpasample.sdjpademo.domain.Book;

import java.util.List;

public interface BookDoa {
    List<Book> findAllBooks(int pageSize, int offset);

    List<Book> findAllBooks();

    Book getById(Long id);

    Book findBookByTitle(String title);

    Book saveNewBook(Book book);

    Book updateBook(Book book);

    void deleteBookById(Long id);
}

```

```java
package chamara.springdatajpasample.sdjpademo.doa;

import chamara.springdatajpasample.sdjpademo.domain.Book;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class BookDoaJDBCTemplate implements BookDoa {
    private final JdbcTemplate jdbcTemplate;

    public BookDoaJDBCTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Book> findAllBooks(int pageSize, int offset) {
        return jdbcTemplate.query("SELECT * FROM book LIMIT ? OFFSET ?", getBookMapper(), pageSize, offset);
    }

    @Override
    public List<Book> findAllBooks() {
        return jdbcTemplate.query("SELECT * FROM book", getBookMapper());
    }

    @Override
    public Book getById(Long id) {
        return jdbcTemplate.queryForObject("SELECT * FROM book where id = ?", getBookMapper(), id);
    }

    @Override
    public Book findBookByTitle(String title) {
        return jdbcTemplate.queryForObject("SELECT * FROM book where title = ?", getBookMapper(), title);
    }

    @Override
    public Book saveNewBook(Book book) {
        jdbcTemplate.update("INSERT INTO book (isbn, publisher, title, author_id) VALUES (?, ?, ?, ?)",
                book.getIsbn(), book.getPublisher(), book.getTitle(), book.getAuthorId());

        Long createdId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);

        return this.getById(createdId);
    }

    @Override
    public Book updateBook(Book book) {
        jdbcTemplate.update("UPDATE book set isbn = ?, publisher = ?, title = ?, author_id = ? where id = ?",
                book.getIsbn(), book.getPublisher(), book.getTitle(), book.getAuthorId(), book.getId());

        return this.getById(book.getId());
    }

    @Override
    public void deleteBookById(Long id) {
        jdbcTemplate.update("DELETE from book where id = ?", id);
    }

    private BookMapper getBookMapper() {
        return new BookMapper();
    }
}

```

```java

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

```

## 115 - Find All Books Using Pagable

```java
package chamara.springdatajpasample.sdjpademo.doa;

import chamara.springdatajpasample.sdjpademo.domain.Book;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookDoa {
    List<Book> findAllBooks(Pageable pageable);

    List<Book> findAllBooks(int pageSize, int offset);

    List<Book> findAllBooks();

    Book getById(Long id);

    Book findBookByTitle(String title);

    Book saveNewBook(Book book);

    Book updateBook(Book book);

    void deleteBookById(Long id);
}

```

```java

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
```

```java
package chamara.springdatajpasample.sdjpademo.doa;

import chamara.springdatajpasample.sdjpademo.domain.Book;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class BookDoaJDBCTemplate implements BookDoa {
    private final JdbcTemplate jdbcTemplate;

    public BookDoaJDBCTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Book> findAllBooks(Pageable pageable) {
        return jdbcTemplate.query("SELECT * FROM book LIMIT ? OFFSET ?", getBookMapper(), pageable.getPageSize(), pageable.getOffset());
    }

    @Override
    public List<Book> findAllBooks(int pageSize, int offset) {
        return jdbcTemplate.query("SELECT * FROM book LIMIT ? OFFSET ?", getBookMapper(), pageSize, offset);
    }

    @Override
    public List<Book> findAllBooks() {
        return jdbcTemplate.query("SELECT * FROM book", getBookMapper());
    }

    @Override
    public Book getById(Long id) {
        return jdbcTemplate.queryForObject("SELECT * FROM book where id = ?", getBookMapper(), id);
    }

    @Override
    public Book findBookByTitle(String title) {
        return jdbcTemplate.queryForObject("SELECT * FROM book where title = ?", getBookMapper(), title);
    }

    @Override
    public Book saveNewBook(Book book) {
        jdbcTemplate.update("INSERT INTO book (isbn, publisher, title, author_id) VALUES (?, ?, ?, ?)",
                book.getIsbn(), book.getPublisher(), book.getTitle(), book.getAuthorId());

        Long createdId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);

        return this.getById(createdId);
    }

    @Override
    public Book updateBook(Book book) {
        jdbcTemplate.update("UPDATE book set isbn = ?, publisher = ?, title = ?, author_id = ? where id = ?",
                book.getIsbn(), book.getPublisher(), book.getTitle(), book.getAuthorId(), book.getId());

        return this.getById(book.getId());
    }

    @Override
    public void deleteBookById(Long id) {
        jdbcTemplate.update("DELETE from book where id = ?", id);
    }

    private BookMapper getBookMapper() {
        return new BookMapper();
    }
}

```

## 116 - Find All Books Order By Title

## 117 - Hibernate Code Review

## 118 - Paging with Hibernate

## 119 - Sorting with Hibernate

## 120 - Paging with Spring Data JPA

## 121 - Sorting with Spring Data JPA

## 122 - Query Paging and Sorting with Spring Data JPA

              