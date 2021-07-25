package com.mytest.webapi.repository;

import com.mytest.webapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String name);
    Optional<User> findByUsernameOrderByEmail(String name);
    User findByEmail(String email);
}
