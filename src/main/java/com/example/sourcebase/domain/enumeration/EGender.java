package com.example.sourcebase.domain.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public enum EGender {
    MALE("Male"),
    FEMALE("Female"),
    OTHER("Other");
    String name;
}
