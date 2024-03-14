package com.footballgg.server.post.service;

import com.footballgg.server.post.domain.Post;
import com.footballgg.server.post.dto.SavePostRequest;
import com.footballgg.server.post.dto.UpdatePostRequest;
import com.footballgg.server.post.repository.PostRepository;
import com.footballgg.server.user.domain.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostServiceImpl implements PostService{
    private final PostRepository postRepository;

    @Override
    @Transactional
    public Post savePost(SavePostRequest savePostRequest, User user) {
        Post post = Post.builder()
                .title(savePostRequest.getTitle())
                .content(savePostRequest.getContent())
                .categoryId(savePostRequest.getCategoryId())
                .user(user)
                .build();
        postRepository.save(post);
        return post;
    }

    @Override
    @Transactional
    public Boolean deletePost(Long postId) {
        Optional<Post> post = postRepository.findPostByPostId(postId);
        if(post.isPresent()) { // 게시글이 존재할 경우
            postRepository.deletePostByPostId(postId);
            return true;

        }
        else
            return false;
    }

    @Override
    @Transactional
    public Post updatePost(UpdatePostRequest updatePostRequest, User user) {
        Post post = postRepository.findPostByPostId(updatePostRequest.getPostId()).get();
        Post updatedPost = Post.builder()
                .postId(post.getPostId())
                .view(post.getView())
                .categoryId(post.getCategoryId())
                .title(updatePostRequest.getTitle())
                .content(updatePostRequest.getContent())
                .user(post.getUser())
                .build();
        Post result = postRepository.save(updatedPost);
        return result;
    }
}
