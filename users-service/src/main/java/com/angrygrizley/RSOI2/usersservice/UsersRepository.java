package com.angrygrizley.RSOI2.usersservice;

import com.angrygrizley.RSOI2.usersservice.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<User, Long> {
    User findByLogin(String login);
}
