### 61 - Introduction

![img.png](img.png)

### 62 - Introduction to DAO Pattern

![img_1.png](img_1.png)
![img_2.png](img_2.png)
![img_3.png](img_3.png)
![img_4.png](img_4.png)
![img_5.png](img_5.png)

### 63 - Create Author DAO

let's create the DAO interface and the implementation class for the `Author` entity.

```java
package chamara.springdatajpasample.sdjpademo.doa;

import chamara.springdatajpasample.sdjpademo.domain.Author;

public interface AuthorDoa {
    Author getAuthorById(Long id);
}
```

```java
package chamara.springdatajpasample.sdjpademo.doa;

import chamara.springdatajpasample.sdjpademo.domain.Author;
import org.springframework.stereotype.Component;

@Component
public class AuthorDoaImpl implements AuthorDoa {
    @Override
    public Author getAuthorById(Long id) {
        return null;
    }
}

```

let's create a test class to test the `AuthorDoa` implementation.

```java
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
    void itShouldName() {
        Author author = authorDoa.getAuthorById(1L);
        assertThat(author).isNotNull();
    }
}
```

### 64 - Implement Get Author By Id

```java
package chamara.springdatajpasample.sdjpademo.doa;

import chamara.springdatajpasample.sdjpademo.domain.Author;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Component
public class AuthorDoaImpl implements AuthorDoa {

    private final DataSource dataSource;

    public AuthorDoaImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Author getAuthorById(Long id) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM author WHERE id = " + id);
            if (resultSet.next()) {
                Author author = new Author();
                author.setId(resultSet.getLong("id"));
                author.setFirstName(resultSet.getString("first_name"));
                author.setLastName(resultSet.getString("last_name"));
                return author;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}

```

### 65 - Release Database Resources

![img_8.png](img_8.png)

springboot will create a connection pool and manage the connections. so we don't need to close the connection manually.
but we need to close the `Statement` and `ResultSet` manually.

how to check the connection pool is working?

```sql
show processlist;
```

let's close the `Statement` and `ResultSet` manually.

```java
package chamara.springdatajpasample.sdjpademo.doa;

import chamara.springdatajpasample.sdjpademo.domain.Author;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Component
public class AuthorDoaImpl implements AuthorDoa {

    private final DataSource dataSource;

    public AuthorDoaImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Author getAuthorById(Long id) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM author WHERE id = " + id);
            if (resultSet.next()) {
                Author author = new Author();
                author.setId(resultSet.getLong("id"));
                author.setFirstName(resultSet.getString("first_name"));
                author.setLastName(resultSet.getString("last_name"));
                return author;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                // Check if the ResultSet object is not null
                if (resultSet != null) {
                    // If it's not null, close the ResultSet
                    resultSet.close();
                }
                // Check if the Statement object is not null
                if (statement != null) {
                    // If it's not null, close the Statement
                    statement.close();
                }
                // Check if the Connection object is not null
                if (connection != null) {
                    // If it's not null, close the Connection
                    connection.close();
                }
            } catch (SQLException e) {
                // If any SQLException occurs during the closing of ResultSet, Statement, or Connection,
                // it's caught and a new RuntimeException is thrown with the caught exception as its cause.
                throw new RuntimeException(e);
            }
        }
    }
```

### 66 - IntelliJ Database Configuration

let's configure the database in the IntelliJ IDEA.

![img_9.png](img_9.png)

### 67 - Using Prepared Statements

why we should use prepared statements instead of statements?

- SQL Injection
- Performance

let's use prepared statements instead of statements.

```java
package chamara.springdatajpasample.sdjpademo.doa;

import chamara.springdatajpasample.sdjpademo.domain.Author;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class AuthorDoaImpl implements AuthorDoa {

    private final DataSource dataSource;

    public AuthorDoaImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Author getAuthorById(Long id) {
        Connection connection = null;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM author WHERE id = ?");
            preparedStatement.setLong(1, id);
//            this is not safe to use in production
//            this could lead to SQL injection
//            resultSet = statement.executeQuery("SELECT * FROM author WHERE id = " + id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Author author = new Author();
                author.setId(resultSet.getLong("id"));
                author.setFirstName(resultSet.getString("first_name"));
                author.setLastName(resultSet.getString("last_name"));
                return author;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (connection != null) {
                    connection.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }
}
```

### 68 - Refactoring Duplicate Code

```java
package chamara.springdatajpasample.sdjpademo.doa;

import chamara.springdatajpasample.sdjpademo.domain.Author;

public interface AuthorDoa {
    Author getAuthorById(Long id);

    Author findAuthorByFirstName(String firstName);
}

```

```java
package chamara.springdatajpasample.sdjpademo.doa;

import chamara.springdatajpasample.sdjpademo.domain.Author;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class AuthorDoaImpl implements AuthorDoa {

    private final DataSource dataSource;

    public AuthorDoaImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private Author getAuthor(ResultSet resultSet) throws SQLException {
        Author author = new Author();
        author.setId(resultSet.getLong("id"));
        author.setFirstName(resultSet.getString("first_name"));
        author.setLastName(resultSet.getString("last_name"));
        return author;
    }

    @Override
    public Author getAuthorById(Long id) {
        Connection connection = null;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM author WHERE id = ?");
            preparedStatement.setLong(1, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return getAuthor(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                closeAllConnections(connection, resultSet, preparedStatement);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    private void closeAllConnections(Connection connection, ResultSet resultSet, PreparedStatement preparedStatement) throws SQLException {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (connection != null) {
                connection.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Author findAuthorByFirstName(String firstName) {
        Connection connection = null;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM author WHERE first_name = ?");
            preparedStatement.setString(1, firstName);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return getAuthor(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                closeAllConnections(connection, resultSet, preparedStatement);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}

```

```java
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
```

### 69 - Save New Author

```java
package chamara.springdatajpasample.sdjpademo.doa;

import chamara.springdatajpasample.sdjpademo.domain.Author;

public interface AuthorDoa {
    Author getAuthorById(Long id);

    Author findAuthorByFirstName(String firstName);

    Author saveAuthor(Author author);
}

```

```java
package chamara.springdatajpasample.sdjpademo.doa;

@Override
public Author saveAuthor(Author author) {
    Connection connection = null;
    ResultSet resultSet = null;
    PreparedStatement preparedStatement = null;
    try {
        connection = dataSource.getConnection();
        preparedStatement = connection.prepareStatement("INSERT INTO author (first_name, last_name) VALUES (?, ?)");
        preparedStatement.setString(1, author.getFirstName());
        preparedStatement.setString(2, author.getLastName());
        preparedStatement.executeUpdate();
        preparedStatement = connection.prepareStatement("SELECT * FROM author WHERE first_name = ?");
        preparedStatement.setString(1, author.getFirstName());
        resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return getAuthor(resultSet);
        }
    } catch (SQLException e) {
        throw new RuntimeException(e);
    } finally {
        try {
            closeAllConnections(connection, resultSet, preparedStatement);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    return null;
}
```

```java
package chamara.springdatajpasample.sdjpademo.doa;

@Test
void itShouldSaveAuthor() {
    Author author = new Author();
    author.setFirstName("John");
    author.setLastName("Doe");
    Author savedAuthor = authorDoa.saveAuthor(author);
    assertThat(savedAuthor).isNotNull();
}
```

### 70 - Update Author

let's write the test case for the `update` method.

```java
package chamara.springdatajpasample.sdjpademo.doa;

@Test
void itShouldUpdateAuthor() {
    // given
    Author author = new Author();
    author.setFirstName("John");
    author.setLastName("Doe");
    Author savedAuthor = authorDoa.saveAuthor(author);
    savedAuthor.setLastName("Updated");

    // when
    Author updateAuthor = authorDoa.updateAuthor(savedAuthor);
    // then
    assertThat(savedAuthor.getLastName()).isEqualTo(updateAuthor.getLastName());
}
```

let's add the new method to the `AuthorDoa` interface.

```java
package chamara.springdatajpasample.sdjpademo.doa;

import chamara.springdatajpasample.sdjpademo.domain.Author;

public interface AuthorDoa {
    Author getAuthorById(Long id);

    Author findAuthorByFirstName(String firstName);

    Author saveAuthor(Author author);

    Author updateAuthor(Author author);
}
```

let's implement the `update` method.

```java

@Override
public Author updateAuthor(Author author) {
    Connection connection = null;
    ResultSet resultSet = null;
    PreparedStatement preparedStatement = null;
    try {
        connection = dataSource.getConnection();
        preparedStatement = connection.prepareStatement("UPDATE author SET first_name = ?, last_name = ? WHERE id = ?");
        preparedStatement.setString(1, author.getFirstName());
        preparedStatement.setString(2, author.getLastName());
        preparedStatement.setLong(3, author.getId());
        preparedStatement.executeUpdate();
        preparedStatement = connection.prepareStatement("SELECT * FROM author WHERE first_name = ?");
        preparedStatement.setString(1, author.getFirstName());
        resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return getAuthor(resultSet);
        }
    } catch (SQLException e) {
        throw new RuntimeException(e);
    } finally {
        try {
            closeAllConnections(connection, resultSet, preparedStatement);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    return null;
}
```

### 71 - Delete Author

```java
package chamara.springdatajpasample.sdjpademo.doa;

import chamara.springdatajpasample.sdjpademo.domain.Author;

public interface AuthorDoa {
    Author getAuthorById(Long id);

    Author findAuthorByFirstName(String firstName);

    Author saveAuthor(Author author);

    Author updateAuthor(Author author);

    Author deleteAuthor(Long id);
}

```

```java
package chamara.springdatajpasample.sdjpademo.doa;

@Override
public Author deleteAuthor(Long id) {

    Connection connection = null;
    ResultSet resultSet = null;
    PreparedStatement preparedStatement = null;
    Author deletedAuthor = null;
    try {
        connection = dataSource.getConnection();
        preparedStatement = connection.prepareStatement("SELECT * FROM author WHERE id = ?");
        preparedStatement.setLong(1, id);
        resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            deletedAuthor = getAuthor(resultSet);
        }
        preparedStatement = connection.prepareStatement("DELETE FROM author WHERE id = ?");
        preparedStatement.setLong(1, id);
        preparedStatement.executeUpdate();
        return deletedAuthor;
    } catch (SQLException e) {
        throw new RuntimeException(e);
    } finally {
        try {
            closeAllConnections(connection, resultSet, preparedStatement);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
```

```java
package chamara.springdatajpasample.sdjpademo.doa;

@Test
void itShouldDeleteTheAuthor() {
    // given
    Author author = new Author();
    author.setFirstName("John");
    author.setLastName("Doe");
    Author savedAuthor = authorDoa.saveAuthor(author);
    Long id = savedAuthor.getId();
    // when
    Author deletedAuthorId = authorDoa.deleteAuthor(id);
    // then
    assertThat(savedAuthor.getId()).isEqualTo(deletedAuthorId.getId());
}
```

### 72 - Refactor Author id to Author

let's add `BookDoa` interface and `BookDoaImpl` class.

```java
package chamara.springdatajpasample.sdjpademo.domain;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class Book {
    private String isbn;
    private String title;
    private String publisher;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Transient
    private Author authorId;

    public Book() {

    }

    public Book(String isbn, String title, String publisher, Author authorId) {
        this.isbn = isbn;
        this.title = title;
        this.publisher = publisher;
        this.authorId = authorId;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Author getAuthor() {
        return authorId;
    }

    public void setAuthor(Author authorId) {
        this.authorId = authorId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(id, book.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

```

```java
package chamara.springdatajpasample.sdjpademo.domain;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class Book {
    private String isbn;
    private String title;
    private String publisher;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Transient
    private Author authorId;

    public Book() {

    }

    public Book(String isbn, String title, String publisher, Author authorId) {
        this.isbn = isbn;
        this.title = title;
        this.publisher = publisher;
        this.authorId = authorId;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Author getAuthor() {
        return authorId;
    }

    public void setAuthor(Author authorId) {
        this.authorId = authorId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(id, book.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
```

```java
package chamara.springdatajpasample.sdjpademo.doa;

import chamara.springdatajpasample.sdjpademo.domain.Book;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;

@Component
public class BookDaoImpl implements BookDao {
    private final DataSource source;
    private final AuthorDoa authorDao;

    public BookDaoImpl(DataSource source, AuthorDoa authorDao) {
        this.source = source;
        this.authorDao = authorDao;
    }

    @Override
    public Book getById(Long id) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;

        try {
            connection = source.getConnection();
            ps = connection.prepareStatement("SELECT * FROM book where id = ?");
            ps.setLong(1, id);
            resultSet = ps.executeQuery();

            if (resultSet.next()) {
                return getBookFromRS(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                closeAll(resultSet, ps, connection);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    public Book findBookByTitle(String title) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;

        try {
            connection = source.getConnection();
            ps = connection.prepareStatement("SELECT * FROM book where title = ?");
            ps.setString(1, title);
            resultSet = ps.executeQuery();

            if (resultSet.next()) {
                return getBookFromRS(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                closeAll(resultSet, ps, connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    public Book saveNewBook(Book book) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;

        try {
            connection = source.getConnection();
            ps = connection.prepareStatement("INSERT INTO book (isbn, publisher, title, author_id) VALUES (?, ?, ?, ?)");
            ps.setString(1, book.getIsbn());
            ps.setString(2, book.getPublisher());
            ps.setString(3, book.getTitle());
            if (book.getAuthor() != null) {
                ps.setLong(4, book.getAuthor().getId());
            }
            ps.execute();

            Statement statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT LAST_INSERT_ID()");

            if (resultSet.next()) {
                Long savedId = resultSet.getLong(1);
                return this.getById(savedId);
            }

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                closeAll(resultSet, ps, connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    public Book updateBook(Book book) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;

        try {
            connection = source.getConnection();
            ps = connection.prepareStatement("UPDATE book set isbn = ?, publisher = ?, title = ?, author_id = ? where id = ?");
            ps.setString(1, book.getIsbn());
            ps.setString(2, book.getPublisher());
            ps.setString(3, book.getTitle());
            if (book.getAuthor() != null) {
                ps.setLong(4, book.getAuthor().getId());
            }
            ps.setLong(5, book.getId());
            ps.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                closeAll(resultSet, ps, connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return getById(book.getId());
    }

    @Override
    public void deleteBookById(Long id) {
        Connection connection = null;
        PreparedStatement ps = null;

        try {
            connection = source.getConnection();
            ps = connection.prepareStatement("DELETE from book where id = ?");
            ps.setLong(1, id);
            ps.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                closeAll(null, ps, connection);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    private Book getBookFromRS(ResultSet resultSet) throws SQLException {
        Book book = new Book();
        book.setId(resultSet.getLong(1));
        book.setIsbn(resultSet.getString(2));
        book.setPublisher(resultSet.getString(3));
        book.setTitle(resultSet.getString(4));
        book.setAuthor(authorDao.getAuthorById(resultSet.getLong(5)));

        return book;
    }

    private void closeAll(ResultSet resultSet, PreparedStatement ps, Connection connection) throws SQLException {
        if (resultSet != null) {
            resultSet.close();
        }

        if (ps != null) {
            ps.close();
        }

        if (connection != null) {
            connection.close();
        }
    }
}
```

let's test the `BookDaoImpl` class.

```java
package chamara.springdatajpasample.sdjpademo.doa;

import chamara.springdatajpasample.sdjpademo.domain.Author;
import chamara.springdatajpasample.sdjpademo.domain.Book;
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
    AuthorDoa authorDao;

    @Autowired
    BookDao bookDao;

    @Test
    void testDeleteBook() {
        Book book = new Book();
        book.setIsbn("1234");
        book.setPublisher("Self");
        book.setTitle("my book");
        Author author = new Author();
        author.setId(3L);
        book.setAuthor(author);
        Book saved = bookDao.saveNewBook(book);

        bookDao.deleteBookById(saved.getId());

        Book deleted = bookDao.getById(saved.getId());

        assertThat(deleted).isNull();
    }

    @Test
    void updateBookTest() {
        Book book = new Book();
        book.setIsbn("1234");
        book.setPublisher("Self");
        book.setTitle("my book");
        Author author = new Author();
        author.setId(3L);
        book.setAuthor(author);
        Book saved = bookDao.saveNewBook(book);

        saved.setTitle("New Book");
        bookDao.updateBook(saved);

        Book fetched = bookDao.getById(saved.getId());

        assertThat(fetched.getTitle()).isEqualTo("New Book");
    }

    @Test
    void testSaveBook() {
        Book book = new Book();
        book.setIsbn("1234");
        book.setPublisher("Self");
        book.setTitle("my book");
        Author author = new Author();
        author.setId(3L);
        book.setAuthor(author);
        Book saved = bookDao.saveNewBook(book);

        assertThat(saved).isNotNull();
    }

    @Test
    void testGetBookByName() {
        Book book = bookDao.findBookByTitle("Clean Code");

        assertThat(book).isNotNull();
    }

    @Test
    void testGetBook() {
        Book book = bookDao.getById(3L);

        assertThat(book.getId()).isNotNull();
    }

    @Test
    void testDeleteAuthor() {
        Author author = new Author();
        author.setFirstName("john");
        author.setLastName("t");

        Author saved = authorDao.saveAuthor(author);

        authorDao.deleteAuthor(saved.getId());

        Author deleted = authorDao.getAuthorById(saved.getId());

        assertThat(deleted).isNull();
    }

    @Test
    void testUpdateAuthor() {
        Author author = new Author();
        author.setFirstName("john");
        author.setLastName("t");

        Author saved = authorDao.saveAuthor(author);

        saved.setLastName("Thompson");
        Author updated = authorDao.updateAuthor(saved);

        assertThat(updated.getLastName()).isEqualTo("Thompson");
    }

    @Test
    void testSaveAuthor() {
        Author author = new Author();
        author.setFirstName("John");
        author.setLastName("Thompson");
        Author saved = authorDao.saveAuthor(author);

        assertThat(saved).isNotNull();
    }

    @Test
    void testGetAuthorByName() {
        Author author = authorDao.findAuthorByFirstName("Craig");

        assertThat(author).isNotNull();
    }

    @Test
    void testGetAuthor() {

        Author author = authorDao.getAuthorById(1L);

        assertThat(author).isNotNull();

    }
}
```
                              