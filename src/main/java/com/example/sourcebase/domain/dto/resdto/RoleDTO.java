package com.example.sourcebase.domain.dto.resdto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@ToString
public class RoleDTO {
    Long id;
    String name;
}
