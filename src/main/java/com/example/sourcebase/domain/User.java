package com.example.sourcebase.domain;

import com.example.sourcebase.domain.enumeration.EGender;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@SQLDelete(sql = "UPDATE users SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    Long id;
    String name;
    @OneToOne(cascade = CascadeType.REMOVE)
    FileInfo fileInfo;
    String email;
    String phoneNumber;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDate dob;
    @Enumerated(EnumType.STRING)
    EGender gender;
    String username;
    String password;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    List<UserRole> userRoles;
    boolean isActive;
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    @ToString.Exclude
    List<UserProject> userProjects;
    @ManyToOne
    Rank rank;
    Long departmentId;

    @OneToMany(mappedBy = "leader", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @ToString.Exclude
    List<Project> project;

}
