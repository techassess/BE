package com.example.sourcebase.domain.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public enum ETypeCriteria {
    ALL_MEMBER("All Member"), // all member can see
    SELF("Self"), // just self can see
    CROSS("Cross"), // just team member can see, include manager, but not self
    MANAGER("Manager"); // just manager can see

    String name;
}
