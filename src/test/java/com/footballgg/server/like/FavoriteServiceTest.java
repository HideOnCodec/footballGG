package com.footballgg.server.like;

import com.footballgg.server.like.service.FavoriteServiceImpl;
import com.footballgg.server.post.domain.Post;
import com.footballgg.server.post.repository.PostRepository;
import com.footballgg.server.user.domain.User;
import com.footballgg.server.user.repository.UserRepository;
import com.footballgg.server.user.usertype.Role;
import com.footballgg.server.user.usertype.UserType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.yml")
public class FavoriteServiceTest {
    private Post post;
    private User user;
    private User user2;
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
        this.user2 = User.builder()
                .userId(2L)
                .userType(UserType.EMAIL)
                .email("test2@naver.com")
                .nickname("test2")
                .role(Role.ROLE_USER)
                .build();
        userRepository.save(this.user2);
        this.post = Post.builder()
                .postId(1L)
                .title("test")
                .content("test")
                .view(1L)
                .categoryId(1)
                .user(user)
                .favoriteCount(0)
                .build();
        postRepository.save(this.post);
    }
    @Test
    @DisplayName("좋아요/취소 테스트")
    void favoritePost(){
        Boolean result = favoriteService.favoritePost(user,post);
        Boolean result2 = favoriteService.favoritePost(user2,post);
        Post post = postRepository.findPostByPostId(1L).get();
        assertThat(post.getFavoriteCount()).isEqualTo(2);
        assertThat(result2).isEqualTo(true);

        Boolean cancel = favoriteService.favoritePost(user,post);
        Post cancelPost = postRepository.findPostByPostId(1L).get();
        assertThat(cancel).isEqualTo(false);
        assertThat(cancelPost.getFavoriteCount()).isEqualTo(0);
    }
}
