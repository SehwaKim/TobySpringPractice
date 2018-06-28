package domain;

import org.junit.Before;
import org.junit.Test;
import springbook.domain.Level;
import springbook.domain.User;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTest {

    private User user;

    @Before
    public void setUp() {
        this.user = new User();
    }

    @Test
    public void upgradLevel() {
        Level[] levels = Level.values();
        for(Level level : levels) {
            if(level.nextLevel() == null) continue;

            user.setLevel(level);
            user.upgradeLevel();
            assertThat(user.getLevel()).isEqualTo(level.nextLevel());
        }
    }

    @Test(expected = IllegalStateException.class)
    public void cannotUpgradeLevel() {
        Level[] levels = Level.values();
        for (Level level : levels) {
            if(level.nextLevel() != null) continue;

            user.setLevel(level);
            user.upgradeLevel();
        }
    }

}
