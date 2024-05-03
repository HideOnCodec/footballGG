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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
        Page<Post> postList = postRepository.findAll(PageRequest.of(pageable.getPageNumber(), 10, Sort.by(Sort.Direction.DESC,"postId")));
        return postList;
    }

    @Override
    @Transactional
    public Page<Post> getPostAllByUser(Pageable pageable, User user) {
        Page<Post> postList = postRepository.findAllByUser(PageRequest.of(pageable.getPageNumber(), 10, Sort.by("postId")),user);
        return postList;
    }

    @Override
    @Transactional
    public Post getPostById(Long postId) {
        return postRepository.findPostByPostId(postId).orElseThrow(()->new ResponseStatusException(HttpStatus.BAD_REQUEST,"존재하지 않는 게시글입니다."));
    }

    @Override
    @Transactional
    public Page<Post> getPostAllByCategoryId(Pageable pageable,int categoryId){
        Page<Post> postList = postRepository
                .findAllByCategoryId(PageRequest.of(pageable.getPageNumber(), 10, Sort.by(Sort.Direction.DESC,"postId")),categoryId);
        return postList;
    }
    @Override
    @Transactional
    public Page<Post> getPopularPostAll(Pageable pageable) {
        // 좋아요 10개 이상 시 인기글
        Page<Post> postList = postRepository
                .findAllByFavoriteCountGreaterThanEqual(PageRequest.of(pageable.getPageNumber(),10,Sort.by(Sort.Direction.DESC,"postId")),10);
        return postList;
    }

    @Override
    @Transactional
    public Page<Post> getPopularPostByCategoryId(Pageable pageable,int categoryId) {
        Page<Post> postList = postRepository
                .findAllByFavoriteCountGreaterThanEqualAndCategoryId(PageRequest.of(pageable.getPageNumber(),10,Sort.by(Sort.Direction.DESC,"postId")),10,categoryId);
        return postList;
    }
}
