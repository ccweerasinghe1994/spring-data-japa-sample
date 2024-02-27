package chamara.springdatajpasample.sdjpademo.doa;

import chamara.springdatajpasample.sdjpademo.domain.Author;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class AuthorDoaImpl implements AuthorDoa {

    private final JdbcTemplate jdbcTemplate;

    public AuthorDoaImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Author getAuthorById(Long id) {
        return jdbcTemplate.queryForObject("SELECT * FROM author WHERE id = ?", getRowMapper(), id);
    }

    @Override
    public Author findAuthorByFirstName(String firstName) {
        return jdbcTemplate.queryForObject("SELECT * FROM author WHERE first_name = ?", getRowMapper(), firstName);
    }

    @Override
    public Author saveAuthor(Author author) {
        jdbcTemplate.update("INSERT INTO author (first_name, last_name) VALUES (?, ?)", author.getFirstName(), author.getLastName());
        return jdbcTemplate.queryForObject("SELECT id, first_name, last_name FROM author WHERE first_name = ? AND last_name = ?", getRowMapper(), author.getFirstName(), author.getLastName());
    }

    @Override
    public Author updateAuthor(Author author) {
        jdbcTemplate
                .update(
                        "UPDATE author SET first_name = ?, last_name = ? WHERE id = ?",
                        author.getFirstName(),
                        author.getLastName(),
                        author.getId());
        return jdbcTemplate.queryForObject("SELECT * FROM author WHERE id = ?", getRowMapper(), author.getId());
    }

    @Override
    public Author deleteAuthor(Long id) {
        return null;
    }

    private RowMapper<Author> getRowMapper() {
        return new AuthorRowMapper();
    }
}
