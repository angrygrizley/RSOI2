package com.angrygrizley.RSOI2.usersservice;

import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.MockitoAnnotations.initMocks;

public class UsersServiceTest {
    private UsersService usersService;

    @Mock
    UsersRepository usersRepository;

    @Before
    public void setUp(){
        initMocks(this);
        usersService = new UsersServiceImplementation(usersRepository);
    }

    @Test
    public void shouldReturnAllUsers(){
        List<User> users= new ArrayList<>();
        User user= new User();
        user.setRating(5);
        user.setLastName("Popovich");
        user.setName("Alesha");
        user.setLogin("alesha994");
        user.setReviewsNum(4);

        given(usersRepository.findAll()).willReturn(users);
        List<User> usersReturned = usersService.getAllUsers();
        assertThat(usersReturned, is(users));
    }

    @Test
    public void shouldCreateUser(){
        User user = new User();
        user.setRating(5);
        user.setLastName("Popovich");
        user.setName("Alesha");
        user.setLogin("alesha994");
        user.setReviewsNum(4);

        given(usersRepository.save(user)).willReturn(user);
        MatcherAssert.assertThat(usersRepository.save(user), is(user));
    }

    @Test
    public void shouldFindUserById() throws UserNotFoundException {
        User user = new User();
        user.setRating(5);
        user.setLastName("Popovich");
        user.setName("Alesha");
        user.setLogin("alesha994");
        user.setReviewsNum(4);

        given(usersRepository.save(user)).willReturn(user);
        given(usersRepository.findById(user.getId())).willReturn(Optional.of(user));

        usersService.createUser(user);
        assertThat(usersService.findUserById(user.getId()), is(user));
    }

    @Test
    public void shouldFindUserByLogin() throws UserNotFoundException {
        User user = new User();
        user.setRating(5);
        user.setLastName("Popovich");
        user.setName("Alesha");
        user.setLogin("alesha994");
        user.setReviewsNum(4);

        given(usersRepository.save(user)).willReturn(user);
        given(usersRepository.findByLogin("alesha994")).willReturn(user);

        usersService.createUser(user);
        assertThat(usersService.findUserByLogin("alesha994"), is(user));
    }
}
