package com.footballgg.server.service.post;

import com.footballgg.server.domain.post.Post;
import com.footballgg.server.dto.post.PostResponse;
import com.footballgg.server.dto.post.SavePostRequest;
import com.footballgg.server.dto.post.UpdatePostRequest;
import com.footballgg.server.domain.user.User;

import java.util.List;

public interface PostService {
    PostResponse savePost(SavePostRequest savePostRequest, User user);
    void deletePost(Long postId, User user);
    PostResponse updatePost(Long postId,UpdatePostRequest updatePostRequest, User user);
    List<String> extractImageUrl(String content);
}
