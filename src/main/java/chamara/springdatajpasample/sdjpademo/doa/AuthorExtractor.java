package chamara.springdatajpasample.sdjpademo.doa;

import chamara.springdatajpasample.sdjpademo.domain.Author;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthorExtractor implements ResultSetExtractor<Author> {
    @Override
    public Author extractData(ResultSet rs) throws SQLException, DataAccessException {
        return new AuthorRowMapper().mapRow(rs, 0);
    }
}
