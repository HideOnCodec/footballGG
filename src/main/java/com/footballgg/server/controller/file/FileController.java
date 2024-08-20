package com.footballgg.server.controller.file;

import com.footballgg.server.service.file.FileServiceImpl;

import com.footballgg.server.service.post.PostServiceImpl;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/file")
public class FileController {
    private final FileServiceImpl fileService;
    private final PostServiceImpl postService;

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("image") MultipartFile multipartFile) throws IOException {
        String imgUrl = fileService.upload(multipartFile); // 파일 업로드 후 url 반환
        fileService.createFileMapping(imgUrl); // 해당 url을 가지는 파일 매핑 생성(아직 post는 null)

        return imgUrl;
    }
}
