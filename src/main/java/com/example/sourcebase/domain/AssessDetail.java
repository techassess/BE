package com.example.sourcebase.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "assess_details")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AssessDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(cascade = CascadeType.REMOVE)
    Assess assess;

    @ManyToOne
    @JoinColumn(name = "criteria_id")
    Criteria criteria;

    @ManyToOne
    @JoinColumn(name = "question_id")
    Question question;

    int value;

    String description;

    boolean isComment;
}
