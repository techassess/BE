package com.example.sourcebase.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Entity
@Table(name = "department_criterias")
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DepartmentCriterias {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false, unique = true)
    Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "criterias_id", nullable = false, unique = true)
    Criteria criteria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false, unique = true)
    Question question;
}
