package com.footballgg.server.repository.comment;

import com.footballgg.server.domain.comment.Comment;
import com.footballgg.server.domain.post.Post;
import com.footballgg.server.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findByPost(Post post, Pageable pageable);
    Page<Comment> findByUser(User user, Pageable pageable);
    @Query("SELECT COUNT(c) FROM Comment c WHERE c.post.postId = :postId")
    long countCommentsByPostId(@Param("postId") Long postId);
    @Query("SELECT COUNT(c) FROM Comment c WHERE c.commentId < :commentId")
    long countCommentsBeforeId(@Param("commentId") Long commentId);
}

