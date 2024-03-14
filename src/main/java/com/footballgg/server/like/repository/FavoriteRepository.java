package com.footballgg.server.like.repository;

import com.footballgg.server.like.domain.Favorite;
import com.footballgg.server.post.domain.Post;
import com.footballgg.server.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite,Long> {
    Optional<Favorite> findFavoriteByUserAndPost(User user, Post post);
}
