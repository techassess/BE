package com.example.sourcebase.domain.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public enum ETypeAssess {
    TEAM("Team"),
    SELF("Self"),
    MANAGER("Manager");

    String name;
}
