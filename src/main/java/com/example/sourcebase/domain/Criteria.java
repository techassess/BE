package com.example.sourcebase.domain;

import com.example.sourcebase.domain.enumeration.ETypeCriteria;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.List;

@Entity
@Table(name = "criterias")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@SQLDelete(sql = "UPDATE criterias SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class Criteria extends BaseEntity implements Comparable<Criteria> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String title;

    @Enumerated(EnumType.STRING)
    ETypeCriteria visibleFor;

    int point;

    @OneToMany(mappedBy = "criteria", fetch = FetchType.LAZY)
    List<Question> questions;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    /**
     * Compare two criteria by their id;
     * <br>
     * if the list of questions is empty,
     * the criteria with questions will be greater than the criteria without questions.
     *
     * @param o the object to be compared.
     * @return
     */
    @Override
    public int compareTo(Criteria o) {
        if (this.getQuestions().isEmpty() && !o.getQuestions().isEmpty()) {
            return 1;
        } else if (!this.getQuestions().isEmpty() && o.getQuestions().isEmpty()) {
            return -1;
        } else {
            return this.getId().compareTo(o.getId());
        }
    }
}