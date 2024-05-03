package com.footballgg.server.service.file;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {
    String upload(MultipartFile multipartFile) throws IOException;
    void fileDelete(String fileName);
}
