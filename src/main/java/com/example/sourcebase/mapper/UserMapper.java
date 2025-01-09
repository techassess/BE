package com.example.sourcebase.mapper;

import com.example.sourcebase.domain.User;
import com.example.sourcebase.domain.dto.reqdto.user.RegisterReqDTO;
import com.example.sourcebase.domain.dto.resdto.user.UserDetailResDTO;
import com.example.sourcebase.domain.dto.resdto.user.UserResDTO;
import com.example.sourcebase.domain.dto.resdto.user.UserRoleResDTO;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    //    @Mappings({
//            @Mapping(target = "dob", source = "dob", dateFormat = "dd-MM-yyyy")
////            ,@Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
//    })
    User toUser(RegisterReqDTO registerReqDTO);

    //    @Mapping(target = "dob", source = "dob", dateFormat = "yyyy-MM-dd")
//    @Mapping(source = "deleted", target = "deleted")
    UserResDTO toUserResDTO(User user);

    UserRoleResDTO toUserRoleResDTO(User user);

    @Mapping(target = "password", ignore = true)
    UserDetailResDTO toUserDetailResDTO(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User partialUpdate(RegisterReqDTO reqDTO, @MappingTarget User user);
}
