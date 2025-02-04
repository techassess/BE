package com.example.sourcebase.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.sourcebase.domain.dto.reqdto.user.RegisterReqDTO;
import com.example.sourcebase.domain.dto.resdto.user.UserResDTO;
import com.example.sourcebase.mapper.RegisterMapper;
import com.example.sourcebase.service.IUserService;
import com.example.sourcebase.util.ErrorCode;
import com.example.sourcebase.util.ResponseData;
import com.example.sourcebase.util.SuccessCode;

import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@AllArgsConstructor
@RequestMapping("api/users")
@CrossOrigin(origins = {"http://192.168.0.152:5000", "http://192.168.0.152:6123", "http://localhost:5000", "http://localhost:6123"})
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class UserRestController {
    IUserService userService;
    RegisterMapper registerMapper;

    @GetMapping()
    public ResponseEntity<ResponseData<?>> getAllUser() {
        return ResponseEntity.ok(
                ResponseData.builder()
                        .code(SuccessCode.GET_SUCCESSFUL.getCode())
                        .message(SuccessCode.GET_SUCCESSFUL.getMessage())
                        .data(userService.getAllUser())
                        .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseData<?>> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(
                ResponseData.builder()
                        .code(SuccessCode.GET_SUCCESSFUL.getCode())
                        .message(SuccessCode.GET_SUCCESSFUL.getMessage())
                        .data(userService.getUserById(id))
                        .build());
    }

    @GetMapping("/current-user/{username}")
    public ResponseEntity<ResponseData<?>> getCurrentUser(@PathVariable String username) {
        return ResponseEntity.ok(
                ResponseData.builder()
                        .code(SuccessCode.GET_SUCCESSFUL.getCode())
                        .message(SuccessCode.GET_SUCCESSFUL.getMessage())
                        .data(userService.getUserDetailBy(username))
                        .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseData<?>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(
                ResponseData.builder()
                        .code(SuccessCode.DELETE_SUCCESSFUL.getCode())
                        .message(SuccessCode.DELETE_SUCCESSFUL.getMessage())
                        .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseData<?>> updateUser(@PathVariable Long id,
                                                      @RequestBody(required = false) RegisterReqDTO request,
                                                      @RequestParam(value = "avatar", required = false) MultipartFile avatar) throws IOException {
        return ResponseEntity.ok(
                ResponseData.builder()
                        .code(SuccessCode.UPDATE_SUCCESSFUL.getCode())
                        .message(SuccessCode.UPDATE_SUCCESSFUL.getMessage())
                        .data(userService.updateUser(id, request, avatar))
                        .build());
    }

    @PutMapping("/updateUserWithAvatar/{id}")
    public ResponseEntity<ResponseData<?>> updateUser2(@PathVariable Long id,
                                                       @RequestParam(value = "user", required = false) RegisterReqDTO request,
                                                       @RequestParam(value = "avatar", required = false) MultipartFile avatar) throws IOException {
        RegisterReqDTO registerReqDTO = registerMapper.readValue(String.valueOf(request), RegisterReqDTO.class);
        return ResponseEntity.ok(
                ResponseData.builder()
                        .code(SuccessCode.UPDATE_SUCCESSFUL.getCode())
                        .message(SuccessCode.UPDATE_SUCCESSFUL.getMessage())
                        .data(userService.updateUser2(id, registerReqDTO, avatar))
                        .build());
    }

    @GetMapping("/{userId}/same-project")
    public ResponseEntity<ResponseData<?>> getAllUserHadSameProject(@PathVariable Long userId) {
        List<UserResDTO> usersHadSameProject = userService.getAllUserHadSameProject(userId);

        if (usersHadSameProject.isEmpty()) {
            return ResponseEntity.status(ErrorCode.USER_NOT_FOUND.getHttpStatus()).body(
                    ResponseData.builder()
                            .code(ErrorCode.USER_NOT_FOUND.getCode())
                            .message(ErrorCode.USER_NOT_FOUND.getMessage())
                            .build());
        }

        return ResponseEntity.ok(
                ResponseData.builder()
                        .code(SuccessCode.GET_SUCCESSFUL.getCode())
                        .message(SuccessCode.GET_SUCCESSFUL.getMessage())
                        .data(usersHadSameProject)
                        .build());
    }

}
