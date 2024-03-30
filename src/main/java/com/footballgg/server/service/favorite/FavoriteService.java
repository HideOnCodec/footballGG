package com.footballgg.server.service.favorite;

import com.footballgg.server.domain.post.Post;
import com.footballgg.server.domain.user.User;

public interface FavoriteService {
    Boolean favoritePost(User user, Post post);
}
