package com.example.sourcebase.repository;

import com.example.sourcebase.domain.DepartmentCriterias;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IDepartmentCriteriasRepository extends JpaRepository<DepartmentCriterias, Long> {

    List<DepartmentCriterias> findByCriteria_IdAndDepartment_Id(Long criteriaId, Long departmentId);
    @Query("DELETE FROM DepartmentCriteria dc WHERE dc.department.id = :departmentId")
    void deleteByDepartmentId(@Param("departmentId") Long departmentId);

}