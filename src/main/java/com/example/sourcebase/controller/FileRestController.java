package com.example.sourcebase.controller;

import com.example.sourcebase.service.IUploadService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/files")
@AllArgsConstructor
@CrossOrigin(origins = {"http://192.168.0.152:5000", "http://192.168.0.152:6123", "http://localhost:5000", "http://localhost:6123"})
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FileRestController {
    IUploadService uploadService;


    @PostMapping
    public ResponseEntity<?> upload(@RequestParam("avatar") MultipartFile avatar) throws IOException {
        return ResponseEntity.ok(uploadService.saveAvatar(avatar));
    }
}
