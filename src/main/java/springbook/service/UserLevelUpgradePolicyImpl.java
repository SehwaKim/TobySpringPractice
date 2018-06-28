package springbook.service;

import org.springframework.beans.factory.annotation.Autowired;
import springbook.dao.UserDao;
import springbook.domain.Level;
import springbook.domain.User;

public class UserLevelUpgradePolicyImpl implements UserLevelUpgradePolicy {
    @Autowired
    private UserDao userDao;

    public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
    public static final int MIN_RECOMMEND_FOR_GOLD = 30;

    @Override
    public boolean canUpgradeLevel(User user) {
        Level currentLevel = user.getLevel();
        switch (currentLevel){
            case BASIC: return (user.getLogin() >= MIN_LOGCOUNT_FOR_SILVER);
            case SILVER: return (user.getRecommend() >= MIN_RECOMMEND_FOR_GOLD);
            case GOLD: return false;
            default: throw new IllegalArgumentException("Unknown Level : "+currentLevel);
        }
    }

    @Override
    public void upgradeLevel(User user) {
        user.upgradeLevel(); // User 에게 니가 갖고 있는 정보를 변경하라고 요청!
        userDao.update(user);
    }
}
