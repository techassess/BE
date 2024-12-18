package com.example.sourcebase.domain.dto.reqdto.user;

import lombok.*;
import lombok.experimental.FieldDefaults;


public record UserLoginReqDTO(String username, String password) {
}
