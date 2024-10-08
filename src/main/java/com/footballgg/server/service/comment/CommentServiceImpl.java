package com.footballgg.server.service.comment;

import com.footballgg.server.domain.comment.Comment;
import com.footballgg.server.domain.post.Post;
import com.footballgg.server.domain.user.User;
import com.footballgg.server.dto.comment.CommentCreateRequest;
import com.footballgg.server.repository.comment.CommentRepository;
import com.footballgg.server.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Override
    public void createComment(CommentCreateRequest createRequest, User user) {
        Post post = postRepository.findById(createRequest.getPostId()).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"존재하지 않는 게시글입니다"));

        commentRepository.save(Comment.builder()
                        .content(createRequest.getContent())
                        .post(post)
                        .user(user)
                        .build());
    }

    @Override
    public void deleteComment(Long commentId, User user) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"존재하지 않는 댓글입니다."));
        if(comment.getUser().getUserId()!=user.getUserId()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"댓글 삭제 권한이 없습니다.");
        }
        commentRepository.delete(comment);
    }

    @Override
    public Page<Comment> getComments(Pageable pageable, Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"존재하지 않는 게시글입니다"));
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
        return commentRepository.findByPost(post,pageRequest);
    }
}
