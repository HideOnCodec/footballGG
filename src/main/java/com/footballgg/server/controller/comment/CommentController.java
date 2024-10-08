package com.footballgg.server.controller.comment;

import com.footballgg.server.domain.comment.Comment;
import com.footballgg.server.domain.user.User;
import com.footballgg.server.dto.comment.CommentCreateRequest;
import com.footballgg.server.dto.comment.CommentResponse;
import com.footballgg.server.repository.comment.CommentRepository;
import com.footballgg.server.service.comment.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;
    private final CommentRepository commentRepository;

    /** 댓글 생성하기 */
    @PostMapping()
    @ResponseBody
    public ResponseEntity<Void> create(@RequestBody CommentCreateRequest request, @AuthenticationPrincipal User user) {
        commentService.createComment(request,user);
        return ResponseEntity.ok().build();
    }

    /** 댓글 삭제하기 */
    @PostMapping("/{commentId}")
    public ResponseEntity<Void> delete(@PathVariable Long commentId, @AuthenticationPrincipal User user){
        commentService.deleteComment(commentId,user);
        return ResponseEntity.ok().build();
    }

    /** 댓글 조회하기*/
    @GetMapping("/list/{postId}")
    public String list(@PathVariable Long postId,
                       @AuthenticationPrincipal User user,
                       @RequestParam(required = false) String type,
                       @RequestParam(required = false) Long commentId, Pageable pageable, Model model) {
        if(type!=null&&type.equals("last")){
            int lastPage = (int) ((commentRepository.countCommentsByPostId(postId)-1)/ 10);
            pageable = PageRequest.of(lastPage,10);
        }
        else if(type!=null&&type.equals("index")&&commentId!=null){
            int lastPage = (int) ((commentRepository.countCommentsBeforeId(commentId)-1)/ 10);
            pageable = PageRequest.of(lastPage,10);
        }
        Page<Comment> comments = commentService.getComments(pageable, postId);
        model.addAttribute("comments", comments);
        model.addAttribute("endCommentPage",comments.getTotalPages()-1);
        model.addAttribute("startComment",comments.getNumber()-5);
        model.addAttribute("endComment",comments.getNumber()+5);
        model.addAttribute("userId",user == null ? -1 : user.getUserId());
        model.addAttribute("postId",postId);
        return "fragments/comment";
    }
}
