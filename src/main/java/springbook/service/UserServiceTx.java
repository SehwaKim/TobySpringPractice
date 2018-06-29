package springbook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import springbook.domain.User;

public class UserServiceTx implements UserService {
    private UserService userService;
    @Autowired
    PlatformTransactionManager transactionManager;

    public UserServiceTx(UserService userService){
        this.userService = userService;
    }

    @Override
    public void upgradeLevels() {
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try{
            userService.upgradeLevels();
            transactionManager.commit(status);
        }catch (RuntimeException e){
            transactionManager.rollback(status);
            throw e;
        }
    }

    @Override
    public void add(User user) {
        userService.add(user);
    }
}
