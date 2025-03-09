package com.example.sourcebase.domain;

import com.example.sourcebase.domain.enumeration.ETypeAssess;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "assesses", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "to_user_id", "project_id"})})
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@SQLDelete(sql = "UPDATE assesses SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class Assess extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne
    @JoinColumn(name = "to_user_id")
    User toUser;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDate assessmentDate;

    @Enumerated(EnumType.STRING)
    ETypeAssess assessmentType;

    int totalPoint;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    Project project;

    @OneToMany(mappedBy = "assess", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<AssessDetail> assessDetails = new ArrayList<>();

    public void addAssessDetail(AssessDetail assessDetail) {
        assessDetails.add(assessDetail);
        assessDetail.setAssess(this);
    }
}
