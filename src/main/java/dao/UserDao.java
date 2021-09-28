package dao;

import models.User;

public interface UserDao {
    User login(User user);
    User register(User user);
}
