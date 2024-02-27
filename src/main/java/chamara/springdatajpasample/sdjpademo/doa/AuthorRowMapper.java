package chamara.springdatajpasample.sdjpademo.doa;

import chamara.springdatajpasample.sdjpademo.domain.Author;
import chamara.springdatajpasample.sdjpademo.domain.Book;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AuthorRowMapper implements RowMapper<Author> {
    @Override
    public Author mapRow(ResultSet resultSet, int i) throws SQLException {
        resultSet.next();
        Author author = new Author();
        author.setId(resultSet.getLong("id"));
        author.setFirstName(resultSet.getString("first_name"));
        author.setLastName(resultSet.getString("last_name"));

        if (resultSet.getString("isbn") != null) {
            author.setBooks(new ArrayList<>());
            author.getBooks().add(mapBooks(resultSet));
        }
        while (resultSet.next()) {
            author.getBooks().add(mapBooks(resultSet));
        }

        return author;
    }

    private Book mapBooks(ResultSet resultSet) throws SQLException {
        Book book = new Book();
        book.setId(resultSet.getLong(4));
        book.setTitle(resultSet.getString(5));
        book.setIsbn(resultSet.getString(6));
        book.setPublisher(resultSet.getString(7));
        book.setAuthorId(resultSet.getLong(1));
        return book;
    }
}
