package com.example.sourcebase.mapper;

import com.example.sourcebase.domain.User;
import com.example.sourcebase.domain.dto.reqdto.user.RegisterReqDTO;
import com.example.sourcebase.domain.dto.resdto.user.UserDetailResDTO;
import com.example.sourcebase.domain.dto.resdto.user.UserResDTO;
import com.example.sourcebase.domain.dto.resdto.user.UserRoleResDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface UserMapper {
    //    @Mappings({
//            @Mapping(target = "dob", source = "dob", dateFormat = "dd-MM-yyyy")
////            ,@Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
//    })
    User toUser(RegisterReqDTO registerReqDTO);

    //    @Mapping(target = "dob", source = "dob", dateFormat = "yyyy-MM-dd")
//    @Mapping(source = "deleted", target = "deleted")
    UserResDTO toUserResDTO(User user);

    UserRoleResDTO toUserRoleResDTO(User user);

    UserDetailResDTO toUserDetailResDTO(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User partialUpdate(RegisterReqDTO reqDTO, @MappingTarget User user);
}
