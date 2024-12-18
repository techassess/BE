package com.example.sourcebase.service;

import com.example.sourcebase.domain.FileInfo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface IUploadService {
    FileInfo saveAvatar(MultipartFile avatar) throws IOException;
}
