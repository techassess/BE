package com.example.sourcebase.domain.dto.resdto.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.io.Serializable;

/**
 * DTO for {@link com.example.sourcebase.domain.Position}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
@Builder
public class PositionResDto implements Serializable {
    private Long id;
    private String name;
}