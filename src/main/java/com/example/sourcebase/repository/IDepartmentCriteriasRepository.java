package com.example.sourcebase.repository;

import com.example.sourcebase.domain.DepartmentCriterias;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IDepartmentCriteriasRepository extends JpaRepository<DepartmentCriterias, Long> {
    List<DepartmentCriterias> findByQuestion_Id(Long questionId);

    List<DepartmentCriterias> findByCriteria_Id(Long criteriaId);

    List<DepartmentCriterias> findByCriteria_IdAndDepartment_Id(Long criteriaId, Long departmentId);

    @Query("select count(dc) > 0 from DepartmentCriterias dc where dc.department.id = :departmentId and dc.criteria.title = :title")
    boolean isCriteriaTitleExistsInDepartment(Long departmentId, String title);

    @Modifying
    @Transactional
    void deleteByDepartmentId(Long departmentId);

}