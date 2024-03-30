package com.footballgg.server.service.post;

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
import org.springframework.stereotype.Service;

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
        int page = pageable.getPageNumber() - 1;
        Page<Post> postList = postRepository.findAll(PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC,"postId")));
        return postList;
    }

    @Override
    @Transactional
    public Page<Post> getPostAllByUser(Pageable pageable, User user) {
        int page = pageable.getPageNumber() - 1;
        Page<Post> postList = postRepository.findAllByUser(PageRequest.of(page, pageable.getPageSize(), Sort.by("postId")),user);
        return postList;
    }

    @Override
    @Transactional
    public Optional<Post> getPostById(Long postId) {
        Optional<Post> post = postRepository.findPostByPostId(postId);
        return post;
    }

    @Override
    @Transactional
    public Page<Post> getPostAllByCategoryId(Pageable pageable,int categoryId){
        int page = pageable.getPageNumber() - 1;
        Page<Post> postList = postRepository
                .findAllByCategoryId(PageRequest.of(page, pageable.getPageSize(), Sort.by(Sort.Direction.DESC,"postId")),categoryId);
        return postList;
    }
    @Override
    @Transactional
    public Page<Post> getPopularPostAll(Pageable pageable) {
        // 좋아요 10개 이상 시 인기글
        int page = pageable.getPageNumber() - 1;
        Page<Post> postList = postRepository
                .findAllByFavoriteCountGreaterThanEqual(PageRequest.of(page,pageable.getPageSize(),Sort.by(Sort.Direction.DESC,"postId")),10);
        return postList;
    }

    @Override
    @Transactional
    public Page<Post> getPopularPostByCategoryId(Pageable pageable,int categoryId) {
        int page = pageable.getPageNumber() - 1;
        Page<Post> postList = postRepository
                .findAllByFavoriteCountGreaterThanEqualAndCategoryId(PageRequest.of(page,pageable.getPageSize(),Sort.by(Sort.Direction.DESC,"postId")),10,categoryId);
        return postList;
    }
}
