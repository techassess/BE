package com.example.sourcebase.service.impl;

import com.cloudinary.Cloudinary;
import com.example.sourcebase.domain.FileInfo;
import com.example.sourcebase.repository.IFileInfoRepository;
import com.example.sourcebase.service.IUploadService;
import com.example.sourcebase.util.UploadUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UploadService implements IUploadService {
 Cloudinary cloudinary;

 IFileInfoRepository fileRepository;

 UploadUtils uploadUtils;

    public FileInfo saveAvatar(MultipartFile avatar) throws IOException {
        if (avatar == null) {
            return null;
        }
        var file = new FileInfo();
        fileRepository.save(file);

        var uploadResult = cloudinary.uploader().upload(avatar.getBytes(), uploadUtils.buildImageUploadParams(file));

        String fileUrl = (String) uploadResult.get("secure_url");
        String fileFormat = (String) uploadResult.get("format");

        file.setFileName(file.getId() + "." + fileFormat);
        file.setFileUrl(fileUrl);
        file.setFileFolder(UploadUtils.IMAGE_UPLOAD_FOLDER);
        file.setCloudId(file.getFileFolder() + "/" + file.getId());
        String fileType = file.getFileName().substring(file.getFileName().lastIndexOf(".") + 1);
        file.setFileType(fileType);
        fileRepository.save(file);
        return file;
    }
}
