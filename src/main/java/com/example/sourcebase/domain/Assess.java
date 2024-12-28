package com.example.sourcebase.domain;

import com.example.sourcebase.domain.enumeration.ETypeAssess;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "assesses")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Assess {
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

    @OneToMany(mappedBy = "assess", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<AssessDetail> assessDetails;
}
