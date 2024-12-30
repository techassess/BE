package com.example.sourcebase.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Set;


@Entity
@Table(name = "criterias")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Criteria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String title;
    int point;
    @OneToMany(mappedBy = "criteria", fetch = FetchType.LAZY)
    List<Question> questions;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    @OneToMany(mappedBy = "criteria", fetch = FetchType.LAZY)
    Set<DepartmentCriterias> departmentCriterias;
}
