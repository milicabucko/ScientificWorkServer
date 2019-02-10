package com.scientificworkserver.service;

import com.scientificworkserver.model.User;
import com.scientificworkserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public static User aktivanUser;

    public User findByUsername(String username){
        User user = userRepository.findByUsername(username);
        return user;
    }

    public User save(User user){
        userRepository.save(user);
        return user;
    }
}
