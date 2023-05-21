package com.restapi.springrestapi.service;


import com.restapi.springrestapi.entity.User;
import com.restapi.springrestapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUser(String userName) {

        return userRepository.findByUserName(userName)
                .orElseThrow(IllegalArgumentException::new);
    }

    public User addUser(User user) {
        String password = user.getPassword();
        String encodedPassword = passwordEncoder.encode(password);
        user.setPassword(encodedPassword);
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(String username) {
        userRepository.deleteByUserName(username);
    }
}
