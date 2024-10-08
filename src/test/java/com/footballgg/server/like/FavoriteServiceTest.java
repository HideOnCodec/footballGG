package com.footballgg.server.like;

import com.footballgg.server.domain.post.Category;
import com.footballgg.server.service.favorite.FavoriteServiceImpl;
import com.footballgg.server.domain.post.Post;
import com.footballgg.server.repository.post.PostRepository;
import com.footballgg.server.domain.user.User;
import com.footballgg.server.repository.user.UserRepository;
import com.footballgg.server.domain.user.Role;
import com.footballgg.server.domain.user.UserType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.yml")
public class FavoriteServiceTest {
    private Post post;
    private User user;
    @Autowired
    private FavoriteServiceImpl favoriteService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void createPost(){
        //given
        this.user = User.builder()
                .userId(1L)
                .userType(UserType.EMAIL)
                .email("test@naver.com")
                .nickname("test")
                .role(Role.ROLE_USER)
                .build();
        userRepository.save(this.user);
        this.post = Post.builder()
                .postId(1L)
                .title("test")
                .content("test")
                .view(1L)
                .category(Category.PREMIER)
                .user(user)
                .build();
        postRepository.save(this.post);
    }
    @Test
    @DisplayName("좋아요/취소 테스트")
    void favoritePost(){
        Boolean result = favoriteService.favoritePost(user,post.getPostId());
        Post post = postRepository.findPostByPostId(1L).get();
        assertThat(post.getFavoriteList().size()).isEqualTo(1);
        assertThat(result).isEqualTo(true);

        Boolean cancel = favoriteService.favoritePost(user,post.getPostId());
        Post cancelPost = postRepository.findPostByPostId(1L).get();
        assertThat(cancel).isEqualTo(false);
        assertThat(cancelPost.getFavoriteList().size()).isEqualTo(0);
    }
}
