package com.example.sourcebase.domain;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Table(name = "questions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Question {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    Long id;

    String title;

    int point;

    @ManyToOne
    Criteria criteria;

    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY)
    List<Answer> answers;
}
