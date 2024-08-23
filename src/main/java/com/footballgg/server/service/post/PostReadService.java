package com.footballgg.server.service.post;

import com.footballgg.server.domain.post.Category;
import com.footballgg.server.domain.post.Post;
import com.footballgg.server.domain.user.User;
import com.footballgg.server.dto.post.PostResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PostReadService {
    Page<Post> getPostAll(Pageable pageable);
    Page<Post> getPostAllByUser(Pageable pageable,User user);
    PostResponse getPostById(Long postId);
    Page<Post> getPostAllByCategory(Pageable pageable, Category category);
    Page<Post> getPopularPostAll(Pageable pageable);
    Page<Post> getPopularPostByCategory(Pageable pageable,Category category);
}
