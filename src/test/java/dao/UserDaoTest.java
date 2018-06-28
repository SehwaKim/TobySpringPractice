package dao;

import springbook.dao.DaoFactory;
import springbook.dao.UserDao;
import springbook.domain.Level;
import springbook.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.SQLException;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@DirtiesContext
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DaoFactory.class)
public class UserDaoTest {
    @Autowired
    private UserDao userDao;
    private User user1;
    private User user2;
    private User user3;

    @Before
    public void setUp(){
        user1 = new User("1", "매", "1235", Level.BASIC, 10, 10, "sehwa@sehwa.com");
        user2 = new User("2", "참새", "1235", Level.SILVER, 10, 10, "sehwa@sehwa.com");
        user3 = new User("3", "독수리", "1235", Level.GOLD, 10, 10, "sehwa@sehwa.com");
    }

    @Test
    public void addAndGet() throws SQLException {

        userDao.deleteAll();

        userDao.add(user1);
        userDao.add(user2);
        assertThat(userDao.getCount(), is(2));

        User userget2 = userDao.get(user2.getId());
        checkSameUser(userget2, user2);

    }

    @Test
    public void count() throws SQLException {
        userDao.deleteAll();
        assertThat(userDao.getCount(), is(0));

        userDao.add(user1);
        assertThat(userDao.getCount(), is(1));

        userDao.add(user2);
        assertThat(userDao.getCount(), is(2));

        userDao.add(user3);
        assertThat(userDao.getCount(), is(3));
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void getUserFailure() throws SQLException {
        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        UserDao userDaoJdbc = context.getBean("userDao", UserDao.class);

        userDaoJdbc.deleteAll();
        assertThat(userDaoJdbc.getCount(), is(0));

        userDaoJdbc.get("unknown_id");
    }

    @Test
    public void getAll() throws SQLException {
        userDao.deleteAll();

        userDao.add(user1);
        List<User> users1 = userDao.getAll();
        assertThat(users1.size(), is(1));

        userDao.add(user2);
        List<User> users2 = userDao.getAll();
        assertThat(users2.size(), is(2));

        userDao.add(user3);
        List<User> users3 = userDao.getAll();
        assertThat(users3.size(), is(3));
    }

    @Test
    public void update(){
        userDao.deleteAll();

        userDao.add(user1);

        user1.setName("북극곰");
        user1.setPassword("1111");
        user1.setLevel(Level.GOLD);
        user1.setLogin(100);
        user1.setRecommend(10000);

        userDao.update(user1);

        User user1update = userDao.get(user1.getId());
        checkSameUser(user1, user1update);
    }

    private void checkSameUser(User user1, User user2){
        assertThat(user1.getId(), is(user2.getId()));
        assertThat(user1.getName(), is(user2.getName()));
        assertThat(user1.getPassword(), is(user2.getPassword()));
        assertThat(user1.getLevel(), is(user2.getLevel()));
        assertThat(user1.getLogin(), is(user2.getLogin()));
        assertThat(user1.getRecommend(), is(user2.getRecommend()));
    }
}
