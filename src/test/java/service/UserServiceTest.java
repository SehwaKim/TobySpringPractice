package service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import springbook.config.DaoFactory;
import springbook.dao.UserDao;
import springbook.domain.Level;
import springbook.domain.User;
import springbook.service.UserService;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DaoFactory.class)
public class UserServiceTest {
    @Autowired
    UserService userService;

    @Autowired
    private UserDao userDao;

    private List<User> users;
    public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
    public static final int MIN_RECOMMEND_FOR_GOLD = 30;

    @Before
    public void setUp(){
        users = Arrays.asList(
            new User("1", "매", "1235", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER-1, 10, "sehwa@sehwa.com"),
            new User("2", "참새", "1235", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER, 10, "sehwa@sehwa.com"),
            new User("3", "독수리", "1235", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD-1, "sehwa@sehwa.com"),
            new User("4", "벌새", "1235", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD, "sehwa@sehwa.com"),
            new User("5", "까치", "1235", Level.GOLD, 100, Integer.MAX_VALUE, "sehwa@sehwa.com")
        );

    }
    @Test
    public void bean(){
        assertThat(this.userService, is(notNullValue()));
    }

    @Test
    public void upgradeLevels(){
        userDao.deleteAll();

        users.forEach(userDao::add);

        userService.upgradeLevels();

        checkLevel(users.get(0), Level.BASIC);
        checkLevel(users.get(1), Level.SILVER);
        checkLevel(users.get(2), Level.SILVER);
        checkLevel(users.get(3), Level.GOLD);
        checkLevel(users.get(4), Level.GOLD);
    }

    private void checkLevel(User user, Level expectedLevel) {
        User userUpdate = userDao.get(user.getId());
        assertThat(userUpdate.getLevel(), is(expectedLevel));
    }
}
