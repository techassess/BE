package com.example.sourcebase.domain.dto.reqdto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class CriteriaReqDTO implements Serializable {

    @NotBlank(message = "Title cannot be blank")
    @NotNull(message = "Title must be not null")
    @Pattern(
            regexp = "[A-Za-zÀ-ỹ\\s]+",
            message = "Title can only contain letters and spaces"
    )
    private String title;

    @NotNull(message = "Point cannot be null")
    @Min(value = 0, message = "Point must be 0 or greater")
    private Integer point;
}
