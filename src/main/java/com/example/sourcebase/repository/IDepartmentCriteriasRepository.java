package com.example.sourcebase.repository;

import com.example.sourcebase.domain.DepartmentCriterias;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IDepartmentCriteriasRepository extends JpaRepository<DepartmentCriterias, Long> {

    List<DepartmentCriterias> findByCriteria_IdAndDepartment_Id(Long criteriaId, Long departmentId);

}