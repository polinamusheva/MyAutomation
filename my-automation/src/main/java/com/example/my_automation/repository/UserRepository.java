package com.example.my_automation.repository;

import com.example.my_automation.entity.User;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u.id FROM User u WHERE LOWER(u.username) = LOWER(:username)")
    Optional<Long> findByUsernameIgnoreCase(@Param("username") String username);
}
