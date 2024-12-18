package com.example.sourcebase.repository;

import com.example.sourcebase.domain.Project;

import com.example.sourcebase.domain.dto.resdto.ProjectResDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IProjectRepository extends JpaRepository<Project,Long> {
    boolean existsByName(String name);
}


