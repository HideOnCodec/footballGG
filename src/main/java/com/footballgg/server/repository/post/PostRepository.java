package com.footballgg.server.repository.post;

import com.footballgg.server.domain.post.Category;
import com.footballgg.server.domain.post.Post;
import com.footballgg.server.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {

    Page<Post> findAllByUser(Pageable pageable,User user); // 특정 유저의 전체 게시글 조회
    Page<Post> findAll(Pageable pageable); // 전체 게시글 조회
    Page<Post> findAllByCategory(Pageable pageable, Category category); // 카테고리에 따른 게시글 리스트 조회
    @Query("SELECT p FROM Post p WHERE SIZE(p.favoriteList) >= :likeCount AND p.modifiedDate >= :date ORDER BY SIZE(p.favoriteList) DESC")
    Page<Post> findPopular(Pageable pageable, int likeCount, LocalDateTime date); // 좋아요 수 10개 이상인 게시글
    // 인기글 카테고리에 따른 분류
    @Query("SELECT p FROM Post p WHERE SIZE(p.favoriteList) >= :likeCount AND p.category = :category AND p.modifiedDate >= :date ORDER BY SIZE(p.favoriteList) DESC")
    Page<Post> findPopularByCategory(Pageable pageable, int likeCount, Category category, LocalDateTime date);
    Optional<Post> findPostByPostId(Long postId); // 게시글 아이디로 조회
    void deletePostByPostId(Long postId); // 게시글 아이디로 삭제

}
