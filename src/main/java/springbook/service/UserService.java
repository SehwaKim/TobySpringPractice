package springbook.service;

import springbook.domain.User;

public interface UserService {
    void upgradeLevels();
    void add(User user);
}
