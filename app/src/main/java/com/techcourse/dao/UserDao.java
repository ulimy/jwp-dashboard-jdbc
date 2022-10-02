package com.techcourse.dao;

import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.techcourse.domain.User;

import nextstep.jdbc.JdbcTemplate;
import nextstep.jdbc.RowMapper;

public class UserDao {

    private static final Logger log = LoggerFactory.getLogger(UserDao.class);

    private static final RowMapper USER_ROW_MAPPER = (rs, rowNum) ->
        new User(
            rs.getLong(1),
            rs.getString(2),
            rs.getString(3),
            rs.getString(4));

    private final DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;

    public UserDao(final JdbcTemplate jdbcTemplate) {
        this.dataSource = jdbcTemplate.getDataSource();
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insert(final User user) {
        final var sql = "insert into users (account, password, email) values (?, ?, ?)";
        jdbcTemplate.update(sql, new Object[] {user.getAccount(), user.getPassword(), user.getEmail()});
    }

    public void update(final User user) {
        final var sql = "update users set id = ?, account = ?, password = ?, email = ? where id = ?";
        jdbcTemplate.update(
            sql, new Object[] {user.getId(), user.getAccount(), user.getPassword(), user.getEmail(), user.getId()});
    }

    public List<User> findAll() {
        final var sql = "select id, account, password, email from users";
        return (List<User>)jdbcTemplate.query(sql, new Object[] {}, USER_ROW_MAPPER);
    }

    public User findById(final Long id) {
        final var sql = "select id, account, password, email from users where id = ?";
        return (User)jdbcTemplate.queryForObject(sql, new Object[] {id}, USER_ROW_MAPPER);
    }

    public User findByAccount(final String account) {
        final var sql = "select id, account, password, email from users where account = ?";
        return (User)jdbcTemplate.queryForObject(sql, new Object[] {account}, USER_ROW_MAPPER);
    }
}
