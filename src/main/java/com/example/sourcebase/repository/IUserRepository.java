package com.example.sourcebase.repository;

import com.example.sourcebase.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IUserRepository extends JpaRepository<User, Long> {
    boolean existsUserByEmailIgnoreCaseOrUsernameIgnoreCaseOrPhoneNumber(String email, String username, String phoneNumber);

    Optional<User> findUserByUsername(String username);

    @Query("SELECT u FROM User u JOIN u.userProjects up WHERE up.project.id = :projectId AND u.id <> :userId")
    List<User> getAllUserHadSameProject(@Param("userId") Long userId, @Param("projectId") Long projectId);


}