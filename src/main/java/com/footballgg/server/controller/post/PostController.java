package com.footballgg.server.controller.post;

import com.footballgg.server.domain.comment.Comment;
import com.footballgg.server.domain.post.Category;
import com.footballgg.server.domain.post.Post;
import com.footballgg.server.domain.user.User;
import com.footballgg.server.dto.post.PostResponse;
import com.footballgg.server.dto.post.SavePostRequest;
import com.footballgg.server.dto.post.UpdatePostRequest;
import com.footballgg.server.service.comment.CommentService;
import com.footballgg.server.service.favorite.FavoriteService;
import com.footballgg.server.service.file.FileService;
import com.footballgg.server.service.post.PostReadService;
import com.footballgg.server.service.post.PostService;
import com.footballgg.server.service.user.security.CustomUserDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/post")
public class PostController {
    private final PostService postService;
    private final PostReadService postReadService;
    private final FileService fileService;
    private final FavoriteService favoriteService;
    private final CommentService commentService;

    /** 게시글 작성 뷰*/
    @GetMapping("/create")
    public String createForm(Model model){
        SavePostRequest savePostRequest = SavePostRequest.builder().build();
        model.addAttribute("savePostRequest", savePostRequest);
        return "post/write";
    }

    /** 게시글 작성 */
    @PostMapping("/create")
    public String create(@Valid SavePostRequest savePostRequest, @AuthenticationPrincipal User user, RedirectAttributes redirectAttributes){
        PostResponse post = postService.savePost(savePostRequest,user);
        log.info("create : user={}, post={}",user.getUserId(),post.getUserId());
        List<String> imgUrl = postService.extractImageUrl(post.getContent());

        if(!imgUrl.isEmpty())
            fileService.updateFileMapping(post.getPostId(),imgUrl);

        redirectAttributes.addAttribute("postId",post.getPostId());
        return "redirect:/post/detail/{postId}";
    }

    /** 게시글 상세 조회 */
    @GetMapping("/detail/{postId}")
    public String detail(@AuthenticationPrincipal User user,@PathVariable Long postId, Model model){
        PostResponse post = postReadService.getPostById(postId);
        Pageable pageable = PageRequest.of(0,10);
        Page<Comment> comments =  commentService.getComments(pageable,post.getPostId());
        model.addAttribute("isWriter", user == null ? false : post.getUserId() == user.getUserId());
        model.addAttribute("post",post);
        model.addAttribute("category",post.getCategory());
        model.addAttribute("isLike",favoriteService.isFavorite(user,post.getPostId()));
        model.addAttribute("comments",comments);
        model.addAttribute("endCommentPage",comments.getTotalPages()-1);
        model.addAttribute("startComment",comments.getNumber()-5);
        model.addAttribute("endComment",comments.getNumber()+5);
        model.addAttribute("userId",user == null ? -1 : user.getUserId());
        model.addAttribute("postId",postId);
        return "post/detail";
    }

    /** 게시글 카테고리별 전체 리스트 조회 */
    @GetMapping("/list/category")
    public String getPostAll(@RequestParam(required = false) Category category, @PageableDefault(page = 0,size = 10) Pageable pageable, Model model){
        Page<Post> postList = category == null ? postReadService.getPostAll(pageable) : postReadService.getPostAllByCategory(pageable,category);
        model.addAttribute("postList",postList);
        model.addAttribute("category",category == null ? "ALL" : category);
        model.addAttribute("endPage",postList.getTotalPages()-1);
        model.addAttribute("start",postList.getNumber()-5);
        model.addAttribute("end",postList.getNumber()+5);
        return "post/list";
    }

    /** 인기글 조회 */
    @GetMapping("/list/popular")
    public String getPopularPost(@RequestParam(required = false) Category category, @PageableDefault(page = 0,size = 10) Pageable pageable, Model model){
        Page<Post> postList = category == null ? postReadService.getPopularPostAll(pageable) : postReadService.getPopularPostByCategory(pageable,category);
        model.addAttribute("postList",postList);
        model.addAttribute("category",category == null ? "ALL" : category);
        model.addAttribute("endPage",postList.getTotalPages()-1);
        model.addAttribute("start",postList.getNumber()-5);
        model.addAttribute("end",postList.getNumber()+5);

        return "post/popular-list";
    }

    /** 게시글 수정하기 뷰 */
    @GetMapping("/update/{postId}")
    public String updateForm(@PathVariable Long postId, Model model){
        PostResponse post = postReadService.getPostById(postId);

        UpdatePostRequest updatePostRequest = UpdatePostRequest.builder().build();
        model.addAttribute("updatePostRequest",updatePostRequest);
        model.addAttribute("post",post);
        model.addAttribute("category",post.getCategory());
        return "post/update";
    }

    /** 게시글 수정하기 */
    @PatchMapping("/update/{postId}")
    public String update(@PathVariable Long postId, @Valid UpdatePostRequest updatePostRequest, @AuthenticationPrincipal User user, Model model){
        PostResponse post = postService.updatePost(postId,updatePostRequest,user);
        model.addAttribute("post",post);
        model.addAttribute("category",post.getCategory());
        return "redirect:/post/detail/{postId}";
    }

    /** 게시글 삭제하기 */
    @PostMapping("/delete/{postId}")
    public String delete(@PathVariable Long postId, @AuthenticationPrincipal User user){
        postService.deletePost(postId,user);
        return "redirect:/post/list/category";
    }

    /** 게시글 좋아요 */
    @PostMapping("/like/{postId}")
    public String like(@PathVariable Long postId, @AuthenticationPrincipal User user,Model model){
        model.addAttribute("isLike",favoriteService.favoritePost(user,postId));
        return "redirect:/post/detail/{postId}";
    }

}
