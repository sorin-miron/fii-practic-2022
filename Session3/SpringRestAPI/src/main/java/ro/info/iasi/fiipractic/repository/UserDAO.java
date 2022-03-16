package ro.info.iasi.fiipractic.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ro.info.iasi.fiipractic.exception.UserNotFoundException;
import ro.info.iasi.fiipractic.model.User;
import ro.info.iasi.fiipractic.repository.mapper.UserRowMapper;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class UserDAO {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(final DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<User> getAllUsers() {
        return jdbcTemplate.query("SELECT * FROM USER", new UserRowMapper());
    }

    public int createUser(String firstName, String  lastName, String email, String password) {
        return jdbcTemplate.update("INSERT INTO USER(FIRST_NAME, LAST_NAME, EMAIL, PASSWORD) VALUES (?, ?, ?, ?)", firstName, lastName, email, password);
    }

    public User getUserById(String id) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM USER WHERE ID = ?", new UserRowMapper(), id);
        } catch (EmptyResultDataAccessException ex) {
            throw new UserNotFoundException(String.format("User with id %s was not found", id));
        }
    }
}
