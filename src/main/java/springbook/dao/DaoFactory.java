package springbook.dao;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class DaoFactory {

    @Bean
    public UserDao userDao(){
        UserDao userDao = new UserDao(jdbcTemplate());
        return userDao;
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }

    @Bean
    public JdbcContext jdbcContext(){
        return new JdbcContext(dataSource());
    }

    @Bean
    public DataSource dataSource() {
        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/springbook?serverTimezone=UTC&useSSL=false";
        String dbUser = "spring";
        String password = "book";

        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setJdbcUrl(url);
        dataSource.setUsername(dbUser);
        dataSource.setPassword(password);
        return dataSource;
    }
}