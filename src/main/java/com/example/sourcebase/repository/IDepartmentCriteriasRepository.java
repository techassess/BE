package com.example.sourcebase.repository;

import com.example.sourcebase.domain.DepartmentCriterias;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IDepartmentCriteriasRepository extends JpaRepository<DepartmentCriterias, Long> {

    List<DepartmentCriterias> findByCriteria_IdAndDepartment_Id(Long criteriaId, Long departmentId);

    @Modifying
    @Transactional
    void deleteByDepartmentId(Long departmentId);

}