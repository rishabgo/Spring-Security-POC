package com.restapi.springrestapi.service;


import com.restapi.springrestapi.entity.MyUserDetails;
import com.restapi.springrestapi.entity.User;
import com.restapi.springrestapi.exception.UserNotFoundException;
import com.restapi.springrestapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new UserNotFoundException("User doesn't exist in DB"));
        return new MyUserDetails(user);
    }
}
