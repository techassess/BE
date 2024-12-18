package com.example.sourcebase.repository;

import com.example.sourcebase.domain.User;
import com.example.sourcebase.domain.UserProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IUserProjectRepository extends JpaRepository<UserProject,Long> {
    @Query("SELECT up.user FROM UserProject up WHERE up.project.id = :projectId")
    List<User> findUsersByProjectId(Long projectId);

    List<UserProject> findAllByProjectId(Long id);
    List<UserProject> findAllByUserId(Long id);
}
