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

### 66 - IntelliJ Database Configuration

### 67 - Using Prepared Statements

### 68 - Refactoring Duplicate Code

### 69 - Save New Author

### 70 - Update Author

### 71 - Delete Author

### 72 - Refactor Author id to Author

###                      

###                      

###                      

###                      

###                      