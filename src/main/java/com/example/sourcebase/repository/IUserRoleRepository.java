package com.example.sourcebase.repository;

import com.example.sourcebase.domain.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserRoleRepository extends JpaRepository<UserRole, Long> {

}
