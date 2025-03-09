package com.example.sourcebase.repository;

import com.example.sourcebase.domain.Criteria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ICriteriaRepository extends JpaRepository<Criteria, Long> {

    boolean existsByTitle(String title);

    @Query("SELECT COUNT(c) > 0 FROM Criteria c WHERE lower(c.title) = lower(:title) AND c.deletedAt IS NULL")
    boolean existsByTitleIgnoreCase(String title);

    @Query("select sum(q.point) from Criteria c " +
            "join c.questions q " +
            "where c.id = :criteriaId" +
            " and q.deletedAt is null" +
            " and c.deletedAt is null")
    Integer getSumOfQuestionsPointByCriteriaId(Long criteriaId);
}
