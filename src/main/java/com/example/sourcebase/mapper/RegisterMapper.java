package com.example.sourcebase.mapper;

import com.example.sourcebase.domain.dto.reqdto.user.RegisterReqDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RegisterMapper {
    RegisterMapper INSTANCE = Mappers.getMapper(RegisterMapper.class);

    RegisterReqDTO readValue(String value, Class<RegisterReqDTO> type);
}
