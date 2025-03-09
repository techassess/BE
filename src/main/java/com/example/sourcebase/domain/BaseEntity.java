package com.example.sourcebase.domain;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@MappedSuperclass
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public abstract class BaseEntity {
    @Column
    LocalDateTime deletedAt = null;
}
