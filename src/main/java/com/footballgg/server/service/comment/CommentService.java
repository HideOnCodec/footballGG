package com.footballgg.server.service.comment;

import com.footballgg.server.domain.comment.Comment;
import com.footballgg.server.domain.user.User;
import com.footballgg.server.dto.comment.CommentCreateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentService {
    void createComment(CommentCreateRequest createRequest, User user);
    void deleteComment(Long commentId, User user);
    Page<Comment> getComments(Pageable pageable, Long postId);
}
