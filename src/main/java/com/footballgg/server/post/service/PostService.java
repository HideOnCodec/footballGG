package com.footballgg.server.post.service;

import com.footballgg.server.post.domain.Post;
import com.footballgg.server.post.dto.SavePostRequest;
import com.footballgg.server.post.dto.UpdatePostRequest;
import com.footballgg.server.user.domain.User;

import java.util.List;

public interface PostService {
    Post savePost(SavePostRequest savePostRequest, User user);
    Boolean deletePost(Long postId);
    Post updatePost(UpdatePostRequest updatePostRequest, User user);
}
