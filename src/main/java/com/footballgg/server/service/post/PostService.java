package com.footballgg.server.service.post;

import com.footballgg.server.domain.post.Post;
import com.footballgg.server.dto.post.SavePostRequest;
import com.footballgg.server.dto.post.UpdatePostRequest;
import com.footballgg.server.domain.user.User;

public interface PostService {
    Post savePost(SavePostRequest savePostRequest, User user);
    void deletePost(Long postId, User user);
    Post updatePost(Long postId,UpdatePostRequest updatePostRequest, User user);
}
