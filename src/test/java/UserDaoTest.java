import springbook.dao.DaoFactory;
import springbook.dao.UserDao;
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
        user1 = new User("1", "매", "1235");
        user2 = new User("2", "참새", "1235");
        user3 = new User("3", "독수리", "1235");
    }

    @Test
    public void addAndGet() throws SQLException {
        User user1 = new User("1", "아이스베어", "1235");
        User user2 = new User("2", "그냥곰", "1235");

        userDao.deleteAll();

        userDao.add(user1);
        userDao.add(user2);
        assertThat(userDao.getCount(), is(2));

        User userget2 = userDao.get(user2.getId());
        assertThat(userget2.getName(), is(user2.getName()));
        assertThat(userget2.getPassword(), is(user2.getPassword()));

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
        UserDao userDao = context.getBean("userDao", UserDao.class);

        userDao.deleteAll();
        assertThat(userDao.getCount(), is(0));

        userDao.get("unknown_id");
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
}
