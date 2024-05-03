package com.footballgg.server.service.post;

import com.footballgg.server.domain.post.Post;
import com.footballgg.server.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PostReadService {
    Page<Post> getPostAll(Pageable pageable);
    Page<Post> getPostAllByUser(Pageable pageable,User user);
    Post getPostById(Long postId);
    Page<Post> getPostAllByCategoryId(Pageable pageable,int categoryId);
    Page<Post> getPopularPostAll(Pageable pageable);
    Page<Post> getPopularPostByCategoryId(Pageable pageable,int categoryId);
}
