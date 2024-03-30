package com.footballgg.server.controller.post;

import com.footballgg.server.service.post.PostServiceImpl;
import com.footballgg.server.service.user.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/post")
public class PostController {
    private final PostServiceImpl postService;
    private final SecurityUtil securityUtil;

    /** 게시글 메인 뷰(전체 조회) */
    @GetMapping("/get/all")
    public String getAll(@PageableDefault(page = 1) Pageable pageable, Model model){

        return "post_main";
    }



}
