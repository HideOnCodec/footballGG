package com.footballgg.server.controller.post;

import com.footballgg.server.domain.post.Post;
import com.footballgg.server.domain.user.User;
import com.footballgg.server.dto.post.SavePostRequest;
import com.footballgg.server.dto.post.UpdatePostRequest;
import com.footballgg.server.service.file.FileServiceImpl;
import com.footballgg.server.service.post.PostReadServiceImpl;
import com.footballgg.server.service.post.PostServiceImpl;
import com.footballgg.server.service.user.security.SecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/post")
@Slf4j
public class PostController {
    private final PostServiceImpl postService;
    private final PostReadServiceImpl postReadService;
    private final FileServiceImpl fileService;
    private final SecurityUtil securityUtil;

    /** 게시글 메인 뷰(전체 조회) */
    @GetMapping("/get/all")
    public String getAll(@PageableDefault(page = 1) Pageable pageable, Model model){

        return "post/list";
    }

    /** 게시글 작성 뷰*/
    @GetMapping("/create")
    public String createForm(Model model){
        SavePostRequest savePostRequest = SavePostRequest.builder().build();
        model.addAttribute("savePostRequest", savePostRequest);
        return "post/write";
    }

    /** 게시글 작성 */
    @PostMapping("/create")
    public String create(@Valid SavePostRequest savePostRequest, RedirectAttributes redirectAttributes){
        Post post = postService.savePost(savePostRequest,securityUtil.getLoginUser());
        List<String> imgUrl = postService.extractImageUrl(post.getContent());


        if(!imgUrl.isEmpty())
            fileService.updateFileMapping(post,imgUrl);

        redirectAttributes.addAttribute("postId",post.getPostId());
        return "redirect:/post/{postId}";
    }

    /** 게시글 상세 조회 */
    @GetMapping("/{postId}")
    public String detail(@PathVariable Long postId, Model model){
        Post post = postReadService.getPostById(postId);
        model.addAttribute("post",post);
        setCategoryName(model,post.getCategoryId());
        return "post/detail";
    }

    /** 게시글 카테고리별 전체 리스트 조회
     * ?category=0 (전체조회) 1 : 프리미어리그 2 : 라리가 3 : 분데스리가 4 : 세리에
     * */
    @GetMapping("/list")
    public String getPostAll(@RequestParam(defaultValue = "0") int categoryId, @PageableDefault(page = 0) Pageable pageable, Model model){
        Page<Post> postList = null;
        log.info("categoryId={}",categoryId);
        if(categoryId == 0){
            postList = postReadService.getPostAll(pageable);
        }
        else{
            postList = postReadService.getPostAllByCategoryId(pageable,categoryId);
        }
        model.addAttribute("postList",postList);
        model.addAttribute("endPage",postList.getTotalPages()-1);
        model.addAttribute("start",postList.getNumber()-5);
        model.addAttribute("end",postList.getNumber()+5);
        model.addAttribute("id",categoryId);
        return "post/list";
    }

    public void setCategoryName(Model model, int categoryId){
        Map<Integer,String> category = new HashMap<>();
        category.put(0,"전체");
        category.put(1,"프리미어리그");
        category.put(2,"라리가");
        category.put(3,"세리에");
        category.put(4,"분데스리가");

        model.addAttribute("category",category.get(categoryId));
    }

    /** 게시글 수정하기 뷰 */
    @GetMapping("/{postId}/update")
    public String updateForm(@PathVariable Long postId, Model model){
        Post post = postReadService.getPostById(postId);

        UpdatePostRequest updatePostRequest = UpdatePostRequest.builder().build();
        model.addAttribute("updatePost",updatePostRequest);
        model.addAttribute("post",post);

        return "post/update";
    }

    /** 게시글 수정하기 */
    @PatchMapping("/update/{postId}")
    public String update(@PathVariable Long postId, UpdatePostRequest updatePostRequest, @AuthenticationPrincipal User user, RedirectAttributes redirectAttributes){
        postService.updatePost(postId,updatePostRequest,user);
        return "redirect:/post/{postId}";
    }

    /** 게시글 삭제하기 */
    @PostMapping("/delete/{postId}")
    public String delete(@PathVariable Long postId, @AuthenticationPrincipal User user){
        log.info("post id 삭제 = {}",postId);
        postService.deletePost(postId,user);
        return "redirect:/post/list";
    }
}
