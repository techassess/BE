package com.example.sourcebase.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link com.example.sourcebase.domain.BaseEntity}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@SuperBuilder
@ToString
public class BaseEntityDto implements Serializable {
    private String createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String modifiedBy;
}