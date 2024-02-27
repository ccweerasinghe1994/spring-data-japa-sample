package chamara.springdatajpasample.sdjpademo.doa;

import chamara.springdatajpasample.sdjpademo.domain.Author;
import org.springframework.jdbc.core.RowMapper;
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

    private RowMapper<Author> getRowMapper() {
        return new AuthorRowMapper();
    }

    ;
}
