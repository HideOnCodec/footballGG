package com.footballgg.server.repository.post;

import com.footballgg.server.domain.post.Post;
import com.footballgg.server.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {

    Page<Post> findAllByUser(Pageable pageable,User user); // 특정 유저의 전체 게시글 조회
    Page<Post> findAll(Pageable pageable); // 전체 게시글 조회
    Page<Post> findAllByCategoryId(Pageable pageable,int categoryId); // 카테고리에 따른 게시글 리스트 조회
    Page<Post> findAllByFavoriteCountGreaterThanEqual(Pageable pageable,int likeCount); // 좋아요 수 10개 이상인 게시글
    // 인기글 카테고리에 따른 분류
    Page<Post> findAllByFavoriteCountGreaterThanEqualAndCategoryId(Pageable pageable,int likeCount,int categoryId);
    Optional<Post> findPostByPostId(Long postId); // 게시글 아이디로 조회
    void deletePostByPostId(Long postId); // 게시글 아이디로 삭제

}
