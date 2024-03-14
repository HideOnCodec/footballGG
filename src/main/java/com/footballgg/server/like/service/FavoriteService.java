package com.footballgg.server.like.service;

import com.footballgg.server.post.domain.Post;
import com.footballgg.server.user.domain.User;

public interface FavoriteService {
    Boolean favoritePost(User user, Post post);
}
