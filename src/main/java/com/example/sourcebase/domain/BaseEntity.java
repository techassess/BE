package com.example.sourcebase.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public abstract class BaseEntity {
    @Column(name="created_by",updatable = false)
    @CreatedBy
    String createdBy;
    @Column(name="create_at", updatable = false)
    @CreatedDate
    LocalDateTime createdAt;
    @Column(name="modified_at")
    @LastModifiedDate
    LocalDateTime modifiedAt;
    @Column(name="modified_by")
    @LastModifiedBy
    String modifiedBy;

}
