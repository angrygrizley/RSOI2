package com.angrygrizley.RSOI2.usersservice;

import com.angrygrizley.RSOI2.usersservice.User;
import com.angrygrizley.RSOI2.usersservice.UserNotFoundException;

import java.util.List;

public interface UsersService {
    User findUserById(Long id) throws UserNotFoundException;
    User findUserByLogin(String login) throws UserNotFoundException;
    List<User> getAllUsers();
    void createUser(User user);
    void deleteUser(Long id);
    void setReviewsNum(Long id, int reviewsNum) throws UserNotFoundException;
}

