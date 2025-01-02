package com.example.sourcebase.repository;

import com.example.sourcebase.domain.Criteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ICriteriaRepository extends JpaRepository<Criteria, Long> {
    @Override
    @Query("SELECT c FROM Criteria c WHERE c.isDeleted = false")
    List<Criteria> findAll();

    @Override
    @Query("SELECT c FROM Criteria c WHERE c.isDeleted = false")
    Page<Criteria> findAll(Pageable pageable);

    @Query("SELECT DISTINCT c FROM Criteria c " +
            "JOIN c.departmentCriterias dc " +
            "WHERE c.isDeleted = false " +
            "AND c.id = :id " +
            "AND dc.department.id = :departmentId")
    Optional<Criteria> findById(@Param("id") Long id, @Param("departmentId") Long departmentId);

    boolean existsByTitle(String title);

    @Query("SELECT COUNT(c) > 0 FROM Criteria c WHERE lower(c.title) = lower(:title) AND c.isDeleted = false")
    boolean existsByTitleIgnoreCase(String title);

    @Query("SELECT c FROM Criteria c " +
            "JOIN c.departmentCriterias dc " +
            "WHERE c.isDeleted = false " +
            "AND dc.department.id = :departmentId")
    List<Criteria> findAllCriteriaByDepartmentId(@Param("departmentId") Long departmentId);

    @Query("select sum(q.point) from Criteria c " +
            "join c.questions q " +
            "where c.id = :criteriaId" +
            " and q.isDeleted = false" +
            " and c.isDeleted = false")
    Integer getSumOfQuestionsPointByCriteriaId(Long criteriaId);
}
