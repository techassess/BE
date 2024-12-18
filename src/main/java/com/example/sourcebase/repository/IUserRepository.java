package com.example.sourcebase.repository;

import com.example.sourcebase.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IUserRepository extends JpaRepository<User, Long> {
    boolean existsUserByEmailIgnoreCaseOrUsernameIgnoreCaseOrPhoneNumber(String email, String username, String phoneNumber);

    Optional<User> findUserByUsername(String username);

    @Query("SELECT u FROM User u " +
            "JOIN u.userProjects up " +
            "JOIN up.project p " +
            "WHERE p.id IN (SELECT up2.project.id FROM UserProject up2 WHERE up2.user.id = :userId) " +
            "AND u.id <> :userId")
    List<User> getAllUserHadSameProject(Long userId);

}