package com.footballgg.server.post.controller;

import com.footballgg.server.post.service.PostServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/post")
public class PostController {
    private final PostServiceImpl postService;

    @GetMapping("/create")




}
