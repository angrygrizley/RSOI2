package com.angrygrizley.RSOI2.usersservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsersServiceImplementation implements UsersService {
    private final UsersRepository usersRepository;

    @Autowired
    public UsersServiceImplementation(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public List<User> getAllUsers() {
        return usersRepository.findAll();
    }

    @Override
    public User findUserById(Long id) throws UserNotFoundException {
        return usersRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public User findUserByLogin(String login) throws UserNotFoundException{
        User user = usersRepository.findByLogin(login);
        if (user == null)
            throw  new UserNotFoundException(login);
        return user;
    }

    @Override
    public void createUser(User user) {
        usersRepository.save(user);
    }

    @Override
    public void deleteUser(Long id){
        usersRepository.deleteById(id);
    }
    
    @Override
    public void setReviewsNum(Long id, int reviewsNum) throws UserNotFoundException {
        User user = usersRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        usersRepository.save(user);
    }
}
