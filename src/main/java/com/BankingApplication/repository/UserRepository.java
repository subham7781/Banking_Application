package com.BankingApplication.repository;

import com.BankingApplication.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsernameOrEmail(String username,String email);
    Optional<User> findByUsername(String email);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);

}
