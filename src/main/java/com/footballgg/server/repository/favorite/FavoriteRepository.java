package com.footballgg.server.repository.favorite;

import com.footballgg.server.domain.favorite.Favorite;
import com.footballgg.server.domain.post.Post;
import com.footballgg.server.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite,Long> {
    Optional<Favorite> findFavoriteByUserAndPost(User user, Post post);
}
