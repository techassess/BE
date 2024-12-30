package com.example.sourcebase.repository;

import com.example.sourcebase.domain.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IDepartmentRepository extends JpaRepository<Department, Long> {
}