package com.example.sourcebase.domain.dto.reqdto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class CriteriaReqDTO {
    @NotBlank(message = "Title cannot be blank")
    @Pattern(
            regexp = "[A-Za-zÀ-ỹ0-9\\s\\p{Punct}]+",
            message = "Title can contain letters, numbers, spaces, and all special characters"
    )
    String title;

    @NotNull(message = "Point cannot be null")
    @Min(value = 1, message = "Point must be 1 or greater")
    Integer point;
}
