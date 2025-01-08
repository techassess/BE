package com.example.sourcebase.service.impl;

import com.example.sourcebase.domain.*;
import com.example.sourcebase.domain.dto.resdto.DepartmentResDTO;
import com.example.sourcebase.domain.dto.resdto.user.UserDetailResDTO;
import com.example.sourcebase.domain.dto.resdto.user.UserProjectResDTO;
import com.example.sourcebase.repository.*;
import com.example.sourcebase.domain.dto.reqdto.user.RegisterReqDTO;
import com.example.sourcebase.domain.dto.reqdto.user.UserLoginReqDTO;
import com.example.sourcebase.domain.dto.resdto.user.UserResDTO;
import com.example.sourcebase.mapper.UserMapper;
import com.example.sourcebase.service.IUserService;
import com.example.sourcebase.util.ErrorCode;

import com.example.sourcebase.util.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.sourcebase.exception.AppException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class UserService implements IUserService, UserDetailsService {
    Log log = new Log();
    IUserRepository userRepository;
    IRoleRepository roleRepository;
    IUserRoleRepository userRoleRepository;
    JwtTokenProvider jwtTokenProvider;
    UserMapper userMapper = UserMapper.INSTANCE;
    IRankRepository rankRepository;
    IPositionRepository positionRepository;
    PasswordEncoder passwordEncoder;
    UploadService uploadService;
    IUserProjectRepository userProjectRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        var userRoles = user.getUserRoles();
        var customUserDetails = new CustomUserDetails(user, userRoles);
        return customUserDetails;
    }

    @Override
    public UserResDTO register(RegisterReqDTO registerReqDTO, MultipartFile avatar) throws IOException {
        if (userRepository.existsUserByEmailIgnoreCaseOrUsernameIgnoreCaseOrPhoneNumber(
                registerReqDTO.getEmail(),
                registerReqDTO.getUsername(),
                registerReqDTO.getPhoneNumber())) {
            log.LogError(ErrorCode.USERNAME_EXISTS);
            throw new AppException(ErrorCode.USERNAME_EXISTS);
        }
        FileInfo fileInfo = uploadService.saveAvatar(avatar);

        registerReqDTO.setPassword(passwordEncoder.encode(registerReqDTO.getPassword()));
        User userNew = userMapper.toUser(registerReqDTO);
        userNew.setFileInfo(fileInfo);
        userNew.setCreatedAt(LocalDateTime.now());
        userNew.setActive(true);
        User createdUser = userRepository.save(userNew);
        saveUserRole(userNew, roleRepository.findById(2L).orElseThrow(() -> new NoSuchElementException("Role not found")));
        saveRank(userNew, registerReqDTO.getPosition(), registerReqDTO.getLevel());
        UserResDTO resultUserResDTO = userMapper.toUserResDTO(createdUser);
        log.LogInfo(SuccessCode.CREATED);

        return resultUserResDTO;
    }

    private void saveRank(User user, String positionInput, String level) {

        Position position = positionRepository.findByName(positionInput);
        if (position == null) {
            throw new IllegalArgumentException("Position not found");
        }
        Rank rank = rankRepository.findByPositionIdAndLevel(position.getId(), level);
        user.setRank(rank);
        userRepository.save(user);
    }

    @Override
    public String login(UserLoginReqDTO userLogin) {
        UserDetails userDetails = loadUserByUsername(userLogin.username());

        if (!passwordEncoder.matches(userLogin.password(), userDetails.getPassword())) {
            throw new IllegalStateException("Wrong password or username");
        }
        UserDetailResDTO userDetailResDto = getUserDetailBy(userLogin.username());

        return jwtTokenProvider.generateToken(userDetailResDto.getId(),
                userDetailResDto.getName(),
                userDetailResDto.getFileInfoResDto() != null ? userDetailResDto.getFileInfoResDto().getFileUrl() : null,
                userDetailResDto.getEmail(),
                userDetailResDto.getPhoneNumber(),
                userDetailResDto.getUsername(),
                userDetailResDto.getRank(),
                userDetailResDto.getUserRoles());
    }

    @Override
    public UserDetailResDTO getUserDetailBy(String username) {
        return userMapper.toUserDetailResDTO(userRepository.findUserByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)));
    }

//    @Override
//    public List<UserResDTO> getAllUser() {
//        List<User> users = userRepository.findAll();
//        List<UserResDTO> userResDTOS = new ArrayList<>();
//        for (User user : users) {
//            UserResDTO userResDTO = userMapper.toUserResDTO(user);
//            if (!user.getUserProjects().isEmpty()) {
//                List<UserProjectResDTO> userProjectResDTOS = new ArrayList<>();
//                for (UserProject userProject : user.getUserProjects()) {
//                    UserProjectResDTO userProjectResDTO = new UserProjectResDTO();
//                    userProjectResDTO.setProjectId(userProject.getProject().getId());
//                    userProjectResDTO.setUserId(userProject.getUser().getId());
//                    userProjectResDTOS.add(userProjectResDTO);
//                }
//                userResDTO.setUserProjects(userProjectResDTOS);
//            }
//            userResDTOS.add(userResDTO);
//        }
//        return userResDTOS;
//    }

//    @Override
//    public UserResDTO getUserById(Long id) {
//        User user = userRepository.findById(id)
//                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
//        UserResDTO userResDTO = userMapper.toUserResDTO(user);
//        if (!user.getUserProjects().isEmpty()) {
//            List<UserProjectResDTO> userProjectResDTOS = new ArrayList<>();
//            for (UserProject userProject : user.getUserProjects()) {
//                UserProjectResDTO userProjectResDTO = new UserProjectResDTO();
//                userProjectResDTO.setProjectId(userProject.getProject().getId());
//                userProjectResDTO.setUserId(userProject.getUser().getId());
//                userProjectResDTOS.add(userProjectResDTO);
//            }
//            userResDTO.setUserProjects(userProjectResDTOS);
//        }
//        return userResDTO;
//    }

    @Override
    public List<UserResDTO> getAllUser() {
        List<User> users = userRepository.findAll();
        List<UserResDTO> userResDTOS = new ArrayList<>();

        for (User user : users) {
            UserResDTO userResDTO = userMapper.toUserResDTO(user);

            if (!user.getUserProjects().isEmpty()) {
                List<UserProjectResDTO> userProjectResDTOS = new ArrayList<>();

                for (UserProject userProject : user.getUserProjects()) {
                    UserProjectResDTO userProjectResDTO = new UserProjectResDTO();
                    userProjectResDTO.setProjectId(userProject.getProject().getId());
                    userProjectResDTO.setUserId(userProject.getUser().getId());

                    // Lấy thông tin về Department từ Project
                    Project project = userProject.getProject();
                    Department department = project.getDepartment(); // Lấy phòng ban từ Project
                    if (department != null) {
                        DepartmentResDTO departmentResDTO = new DepartmentResDTO();
                        departmentResDTO.setId(department.getId());
                        departmentResDTO.setName(department.getName());
                        // Nếu cần thêm các thuộc tính khác của Department, có thể set ở đây
                        userProjectResDTO.setDepartment(departmentResDTO);
                    }

                    userProjectResDTOS.add(userProjectResDTO);
                }

                userResDTO.setUserProjects(userProjectResDTOS);
            }

            userResDTOS.add(userResDTO);
        }

        return userResDTOS;
    }

    @Override
    public UserResDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        UserResDTO userResDTO = userMapper.toUserResDTO(user);

        // Nếu người dùng có dự án, lấy thông tin về dự án và phòng ban
        if (!user.getUserProjects().isEmpty()) {
            List<UserProjectResDTO> userProjectResDTOS = new ArrayList<>();
            for (UserProject userProject : user.getUserProjects()) {
                UserProjectResDTO userProjectResDTO = new UserProjectResDTO();
                userProjectResDTO.setProjectId(userProject.getProject().getId());
                userProjectResDTO.setUserId(userProject.getUser().getId());

                // Lấy thông tin về Department từ Project
                Project project = userProject.getProject();
                Department department = project.getDepartment();  // Lấy phòng ban từ Project
                DepartmentResDTO departmentResDTO = new DepartmentResDTO();
                departmentResDTO.setId(department.getId());
                departmentResDTO.setName(department.getName());
//            departmentResDTO.setDeleted(department.getDeleted());

                userProjectResDTO.setDepartment(departmentResDTO);  // Set thông tin Department vào DTO
                userProjectResDTOS.add(userProjectResDTO);
            }
            userResDTO.setUserProjects(userProjectResDTOS);
        }
        return userResDTO;
    }


    @Override
    public boolean deleteUser(Long id) {
        try {
            Optional<User> userOpt = userRepository.findById(id);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                user.setDeleted(true);
                user.setActive(false);
                userRepository.save(user);
                return true;
            } else {
                throw new AppException(ErrorCode.USER_NOT_FOUND);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }

    @Override
    @Transactional
    public UserResDTO updateUser(Long id, RegisterReqDTO request, MultipartFile avatar) throws IOException {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        FileInfo fileInfo = uploadService.saveAvatar(avatar);
        if (request.getEmail() != null && !request.getEmail().equals(existingUser.getEmail())) {
            if (userRepository.existsUserByEmailIgnoreCaseOrUsernameIgnoreCaseOrPhoneNumber(request.getEmail(), null, null)) {
                throw new IllegalArgumentException("Email already exists");
            }
        }
        if (request.getPhoneNumber() != null && !request.getPhoneNumber().equals(existingUser.getPhoneNumber())) {
            if (userRepository.existsUserByEmailIgnoreCaseOrUsernameIgnoreCaseOrPhoneNumber(null, request.getPhoneNumber(), null)) {
                throw new IllegalArgumentException("Phone already exists");
            }
        }

        User userToUpdate = userMapper.toUser(request);
        existingUser.setFileInfo(fileInfo);
//        existingUser.setName(userToUpdate.getName());
//        existingUser.setPhoneNumber(userToUpdate.getPhoneNumber());
//        existingUser.setEmail(userToUpdate.getEmail());
//        existingUser.setUsername(userToUpdate.getUsername());
//        existingUser.setPassword(userToUpdate.getPassword());
//        existingUser.setGender(userToUpdate.getGender());
//        saveRank(existingUser, request.getPosition(), request.getLevel());
//        existingUser.setDob(userToUpdate.getDob());
//        existingUser.setUserRoles(userToUpdate.getUserRoles());
//        existingUser.setUserProjects(userToUpdate.getUserProjects());

        User updatedUser = userRepository.save(existingUser);
        return userMapper.toUserResDTO(updatedUser);
    }

    @Transactional
    public void saveUserRole(User user, Role role) {
        UserRole userRole = new UserRole(user, role);
        userRoleRepository.save(userRole);
    }

    @Override
    public List<UserResDTO> getAllUserHadSameProject(Long userId) {
        List<User> userList = userRepository.getAllUserHadSameProject(userId);
        List<UserResDTO> userResDTOS = new ArrayList<>();
        for (User user : userList) {
            UserResDTO userResDTO = userMapper.toUserResDTO(user);
            if (!user.getUserProjects().isEmpty()) {
                List<UserProjectResDTO> userProjectResDTOS = new ArrayList<>();
                for (UserProject userProject : user.getUserProjects()) {
                    UserProjectResDTO userProjectResDTO = new UserProjectResDTO();
                    userProjectResDTO.setProjectId(userProject.getProject().getId());
                    userProjectResDTO.setUserId(userProject.getUser().getId());
                    userProjectResDTOS.add(userProjectResDTO);
                }
                userResDTO.setUserProjects(userProjectResDTOS);
            }
            userResDTOS.add(userResDTO);
        }
        return userResDTOS;
    }
}
