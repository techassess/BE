package com.example.sourcebase.repository;

import com.example.sourcebase.domain.Position;
import com.example.sourcebase.domain.Rank;
import jakarta.persistence.Id;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRankRepository extends JpaRepository<Rank, Long> {
    Rank findByPositionIdAndLevel(Long positionId, String level);
}