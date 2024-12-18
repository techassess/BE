package com.example.sourcebase.repository;

import com.example.sourcebase.domain.FileInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IFileInfoRepository  extends JpaRepository<FileInfo, String> {
}
