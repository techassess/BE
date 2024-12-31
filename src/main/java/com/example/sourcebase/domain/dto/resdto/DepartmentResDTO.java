package com.example.sourcebase.domain.dto.resdto;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DepartmentResDTO {
    Long id;
    String name;
    List<CriteriaResDTO> criteria;

}
