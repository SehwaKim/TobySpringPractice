package springbook.dao;

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

public class UserDao {
    private JdbcTemplate jdbcTemplate;

    private RowMapper<User> userRowMapper = new RowMapper<User>() {
        @Override
        public User mapRow(ResultSet resultSet, int i) throws SQLException {
            User user = new User();
            user.setId(resultSet.getString("id"));
            user.setName(resultSet.getString("name"));
            user.setPassword(resultSet.getString("password"));

            return user;
        }
    };

    public UserDao(){}

    public UserDao(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    // context
    /*private void jdbcContextWithStatementStrategy(StatementStrategy strategy) throws SQLException {
        try(Connection c = dataSource.getConnection();
              PreparedStatement ps = strategy.makePreparedStatement(c)){
            ps.executeUpdate();

        }catch (SQLException e){
            throw e;
        }
    }*/

    public void add(User user) throws SQLException {
        /*jdbcContext.workWithStatementStrategy((Connection c) -> {
            PreparedStatement ps = c.prepareStatement("insert into users(id, name, password) values(?,?,?)");
            ps.setString(1,user.getId());
            ps.setString(2,user.getName());
            ps.setString(3,user.getPassword());

            return ps;
        });*/
        jdbcTemplate.update("insert into users(id, name, password) values(?,?,?)", user.getId(), user.getName(), user.getPassword());
    }

    public User get(String id) throws SQLException {
        return jdbcTemplate.queryForObject("select * from users where id = ?", userRowMapper);
    }

//    public void deleteAll() throws SQLException { // context 를 사용하는 client
//        StatementStrategy strategy = new DeleteAllStatement(); // 전략을 선택
//        jdbcContextWithStatementStrategy(strategy); // context 에 전략을 전달
//    }

    public void deleteAll() throws SQLException {
//        jdbcContext.executeSql("delete from users");
//        jdbcTemplate.update("delete from users");
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                return connection.prepareStatement("delete from users");
            }
        });
    }



    public int getCount() throws SQLException {
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

    public List<User> getAll() throws SQLException {
        return jdbcTemplate.query("select * from users order by id asc", userRowMapper);
    }
}
