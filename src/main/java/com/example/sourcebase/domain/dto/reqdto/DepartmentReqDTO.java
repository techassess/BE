package com.example.sourcebase.domain.dto.reqdto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DepartmentReqDTO {
    @NotBlank(message = "Name not be black")
    String name;

}
