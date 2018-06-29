package springbook.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import springbook.dao.JdbcContext;
import springbook.dao.UserDao;
import springbook.dao.UserDaoJdbc;
import springbook.service.*;

import javax.sql.DataSource;

@Configuration
public class DaoFactory {

    @Bean
    public UserService userService(){
        return new UserServiceTx(userServiceImpl());
    }

    @Bean
    public UserService userServiceImpl(){
        return new UserServiceImpl(userDao(), userLevelUpgradePolicy());
    }

    @Bean
    public UserLevelUpgradePolicy userLevelUpgradePolicy(){
        return new UserLevelUpgradePolicyImpl();
    }

    @Bean
    public UserDao userDao(){
        UserDao userDao = new UserDaoJdbc(jdbcTemplate());
        return userDao;
    }

    @Bean
    public PlatformTransactionManager transactionManager(){
        return new DataSourceTransactionManager(dataSource());
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