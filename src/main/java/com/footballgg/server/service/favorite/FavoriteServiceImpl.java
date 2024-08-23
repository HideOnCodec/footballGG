package com.footballgg.server.service.favorite;

import com.footballgg.server.domain.favorite.Favorite;
import com.footballgg.server.repository.favorite.FavoriteRepository;
import com.footballgg.server.domain.post.Post;
import com.footballgg.server.repository.post.PostRepository;
import com.footballgg.server.domain.user.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class FavoriteServiceImpl implements FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final PostRepository postRepository;

    @Override
    public Boolean favoritePost(User user, Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"존재하지 않는 게시글입니다."));
        Optional<Favorite> like = favoriteRepository.findFavoriteByUserAndPost(user,post);
        if(like.isPresent()){ // 이미 좋아요인 경우
            favoriteRepository.delete(like.get());
            return false;
        }
        else{ // 좋아요가 없을 경우
            Favorite createFavorite = Favorite.builder()
                    .user(user)
                    .post(post)
                    .build();
            favoriteRepository.save(createFavorite);
            return true;
        }
    }

    @Override
    public Boolean isFavorite(User user, Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"존재하지 않는 게시글입니다."));
        return favoriteRepository.findFavoriteByUserAndPost(user,post).isPresent();
    }
}
