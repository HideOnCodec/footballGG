package com.footballgg.server.service.file;

import com.footballgg.server.domain.file.FileMapping;
import com.footballgg.server.domain.post.Post;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileService {
    String upload(MultipartFile multipartFile) throws IOException;
    void fileDelete(String fileName);
    void deleteMultiFile(List<String> imgUrlList);
    List<FileMapping> updateFileMapping(Long postId, List<String> fileUrl);
}
