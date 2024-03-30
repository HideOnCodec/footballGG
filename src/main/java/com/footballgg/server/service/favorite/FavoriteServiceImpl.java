package com.footballgg.server.service.favorite;

import com.footballgg.server.domain.favorite.Favorite;
import com.footballgg.server.repository.favorite.FavoriteRepository;
import com.footballgg.server.domain.post.Post;
import com.footballgg.server.repository.post.PostRepository;
import com.footballgg.server.domain.user.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final PostRepository postRepository;
    @Override
    @Transactional
    public Boolean favoritePost(User user, Post post) {
        Optional<Favorite> like = favoriteRepository.findFavoriteByUserAndPost(user,post);
        if(like.isPresent()){ // 이미 좋아요인 경우
            Post updatePost = post.toBuilder()
                    .favoriteCount(post.getFavoriteCount()-1) // 좋아요 -1
                    .build();
            favoriteRepository.delete(like.get());
            postRepository.save(updatePost);
            return false;
        }
        else{ // 좋아요가 없을 경우
            Favorite createFavorite = Favorite.builder()
                    .user(user)
                    .post(post)
                    .build();
            Post updatePost = post.toBuilder()
                    .favoriteCount(post.getFavoriteCount()+1) // 좋아요 +1
                    .build();
            favoriteRepository.save(createFavorite);
            postRepository.save(updatePost);
            return true;
        }
    }
}
