package com.restapi.springrestapi.repository;

import com.restapi.springrestapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUserName(String userName);

    void deleteByUserName(String userName);
}
