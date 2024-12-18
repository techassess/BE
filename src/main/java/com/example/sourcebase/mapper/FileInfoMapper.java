package com.example.sourcebase.mapper;

import com.example.sourcebase.domain.FileInfo;
import com.example.sourcebase.domain.dto.resdto.FileInfoResDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface FileInfoMapper {
    FileInfoMapper INSTANCE = Mappers.getMapper(FileInfoMapper.class);
    FileInfoResDTO toResDto(FileInfo fileInfo);
}
