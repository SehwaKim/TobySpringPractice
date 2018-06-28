package springbook.dao;

import springbook.domain.Level;
import springbook.domain.User;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDaoJdbc implements UserDao {
    private JdbcTemplate jdbcTemplate;

    private RowMapper<User> userRowMapper = new RowMapper<User>() {
        @Override
        public User mapRow(ResultSet resultSet, int i) throws SQLException {
            User user = new User();
            user.setId(resultSet.getString("id"));
            user.setName(resultSet.getString("name"));
            user.setPassword(resultSet.getString("password"));
            user.setLevel(Level.valueOf(resultSet.getInt("level"))); // user.getLevel().intValue()
            user.setLogin(resultSet.getInt("login"));
            user.setRecommend(resultSet.getInt("recommend"));
            user.setEmail(resultSet.getString("email"));

            return user;
        }
    };

    public UserDaoJdbc(){}

    public UserDaoJdbc(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public void add(User user){
        jdbcTemplate.update("insert into users(id, name, password, level, login, recommend, email) values(?,?,?,?,?,?,?)"
                                , user.getId()
                                , user.getName()
                                , user.getPassword()
                                , user.getLevel().intValue()
                                , user.getLogin()
                                , user.getRecommend()
                                , user.getEmail());
    }

    public User get(String id){
        return jdbcTemplate.queryForObject("select * from users where id = ?", new Object[]{id}, userRowMapper);
    }
    public void deleteAll(){
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                return connection.prepareStatement("delete from users");
            }
        });
    }



    public int getCount(){
        return jdbcTemplate.query(new PreparedStatementCreator() {
                    @Override
                    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                        return connection.prepareStatement("select count(*) as cnt from users");
                    }
                }, new ResultSetExtractor<Integer>() {
                    @Override
                    public Integer extractData(ResultSet resultSet) throws SQLException, DataAccessException {
                        resultSet.next();
                        return resultSet.getInt(1);
                    }
                });
    }


    public List<User> getAll(){
        return jdbcTemplate.query("select * from users order by id asc", userRowMapper);
    }

    @Override
    public void update(User user) {
        final String sql = "update users set name = ?, password = ?, level = ?, login = ?, recommend = ?, email = ? where id = ?";
        this.jdbcTemplate.update(sql, user.getName(), user.getPassword(), user.getLevel().intValue(), user.getLogin(), user.getRecommend(), user.getEmail(), user.getId());
    }
}