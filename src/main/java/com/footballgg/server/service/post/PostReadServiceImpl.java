package com.footballgg.server.service.post;

import com.footballgg.server.domain.post.Category;
import com.footballgg.server.dto.post.PostResponse;
import com.footballgg.server.service.favorite.FavoriteServiceImpl;
import com.footballgg.server.domain.post.Post;
import com.footballgg.server.repository.post.PostRepository;
import com.footballgg.server.domain.user.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostReadServiceImpl implements PostReadService{
    private final PostRepository postRepository;
    private final FavoriteServiceImpl likeService;
    @Override
    @Transactional
    public Page<Post> getPostAll(Pageable pageable) {
        return postRepository.findAll(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC,"postId")));
    }

    @Override
    @Transactional
    public Page<Post> getPostAllByUser(Pageable pageable, User user) {
        return postRepository.findAllByUser(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("postId")),user);
    }

    @Override
    @Transactional
    public PostResponse getPostById(Long postId) {
        Post post =  postRepository.findPostByPostId(postId).orElseThrow(()->new ResponseStatusException(HttpStatus.BAD_REQUEST,"존재하지 않는 게시글입니다."));
        return PostServiceImpl.toPostResponse(post);
    }

    @Override
    @Transactional
    public Page<Post> getPostAllByCategory(Pageable pageable, Category category){
        return postRepository
                .findAllByCategory(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC,"postId")),category);
    }

    @Override
    @Transactional
    public Page<Post> getPopularPostAll(Pageable pageable) {
        // 일주일 전까지 글만 포함
        LocalDateTime oneWeekAgo = LocalDateTime.now().minus(1, ChronoUnit.WEEKS);
        // 좋아요 10개 이상 시 인기글
        return postRepository
                .findPopular(PageRequest.of(pageable.getPageNumber(),pageable.getPageSize(),Sort.by(Sort.Direction.DESC,"postId")),1,oneWeekAgo);
    }

    @Override
    @Transactional
    public Page<Post> getPopularPostByCategory(Pageable pageable,Category category) {
        // 일주일 전까지 글만 포함
        LocalDateTime oneWeekAgo = LocalDateTime.now().minus(1, ChronoUnit.WEEKS);
        return postRepository
                .findPopularByCategory(PageRequest.of(pageable.getPageNumber(),pageable.getPageSize(),Sort.by(Sort.Direction.DESC,"postId")),1,category,oneWeekAgo);
    }
}
