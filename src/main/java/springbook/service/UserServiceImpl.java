package springbook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import springbook.dao.UserDao;
import springbook.domain.Level;
import springbook.domain.User;

import java.util.List;

public class UserServiceImpl implements UserService{
    private UserDao userDao;
    private UserLevelUpgradePolicy userLevelUpgradePolicy;

    public UserServiceImpl(){}

    public UserServiceImpl(UserDao userDao, UserLevelUpgradePolicy userLevelUpgradePolicy){
        this.userDao = userDao;
        this.userLevelUpgradePolicy = userLevelUpgradePolicy;
    }

    public void upgradeLevels(){
        List<User> users = userDao.getAll();
        for(User user : users){
            if(userLevelUpgradePolicy.canUpgradeLevel(user)){
                userLevelUpgradePolicy.upgradeLevel(user);
            }
        }
    }

    public void add(User user){
        if(user.getLevel() == null){ user.setLevel(Level.BASIC); }
        userDao.add(user);
    }
}
