package com.example.sourcebase.service;

import com.example.sourcebase.domain.dto.reqdto.user.RegisterReqDTO;
import com.example.sourcebase.domain.dto.reqdto.user.UserLoginReqDTO;
import com.example.sourcebase.domain.dto.resdto.user.UserDetailResDTO;
import com.example.sourcebase.domain.dto.resdto.user.UserResDTO;
import com.example.sourcebase.util.ResponseData;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
public interface IUserService {
    List<UserResDTO> getAllUser();


    UserResDTO register(RegisterReqDTO userNew , MultipartFile avatar) throws IOException;
    String login(UserLoginReqDTO userLogin);
    UserDetailResDTO getUserDetailBy(String username);

    UserResDTO getUserById(Long id);

    boolean deleteUser(Long id);

    UserResDTO updateUser(Long id, RegisterReqDTO request, MultipartFile avatar) throws IOException;

    List<UserResDTO> getAllUserHadSameProject(Long userId);
}
