package com.footballgg.server.post.service;

import com.footballgg.server.post.domain.Post;
import com.footballgg.server.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PostReadService {
    Page<Post> getPostAll(Pageable pageable);
    Page<Post> getPostAllByUser(Pageable pageable,User user);
    Optional<Post> getPostById(Long postId);
    Page<Post> getPostAllByCategoryId(Pageable pageable,int categoryId);
    Page<Post> getPopularPostAll(Pageable pageable);
    Page<Post> getPopularPostByCategoryId(Pageable pageable,int categoryId);
}
