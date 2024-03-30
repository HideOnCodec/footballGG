package com.footballgg.server.post;

import com.footballgg.server.service.favorite.FavoriteServiceImpl;
import com.footballgg.server.domain.post.Post;
import com.footballgg.server.repository.post.PostRepository;
import com.footballgg.server.service.post.PostReadServiceImpl;
import com.footballgg.server.domain.user.User;
import com.footballgg.server.repository.user.UserRepository;
import com.footballgg.server.domain.user.Role;
import com.footballgg.server.domain.user.UserType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // 클래스 단위로 생명주기를 바꾸면 static을 붙일 필요가 없어짐
@TestPropertySource(locations = "classpath:application-test.yml")
public class PostReadServiceTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private FavoriteServiceImpl favoriteService;
    @Autowired
    private PostReadServiceImpl postReadService;

    private Pageable pageable;
    private User user;
    @BeforeAll
    void setting(){
        this.pageable = PageRequest.of(1, 10);
        // user 10명 생성, 각각 게시글 1개씩 생성
        List<User> userList = new ArrayList<>();
        List<Post> postList = new ArrayList<>();

        for(int i=0; i<10; i++){
            User user = User.builder()
                    .role(Role.ROLE_USER)
                    .userType(UserType.EMAIL)
                    .email("test"+i)
                    .nickname("test"+i)
                    .password("test")
                    .build();
            int categoryId = i > 5 ? 1 : 2;
            Post post = Post.builder()
                    .title("test")
                    .view(1L)
                    .content("test")
                    .categoryId(categoryId)
                    .favoriteCount(0)
                    .user(user)
                    .build();

            userList.add(user);
            postList.add(post);
        }
        userRepository.saveAll(userList);
        postRepository.saveAll(postList);

        this.user = userRepository.findById(1L).get();
        List<User> users = userRepository.findAll();
        for(int i=0; i<10; i++){
            Post favoritePost = postRepository.findPostByPostId(1L).get();
            favoriteService.favoritePost(users.get(i),favoritePost);
        }
    }
    @Test
    @DisplayName("모든 게시글 조회")
    void getPostAll(){
        List<Post> posts = postReadService.getPostAll(pageable).getContent();
        assertThat(posts.size()).isEqualTo(10);
    }

    @Test
    @DisplayName("특정 유저의 게시글 전체 조회")
    void getPostAllByUser(){
        List<Post> postList = postReadService.getPostAllByUser(pageable,user).getContent();
        assertThat(postList.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("특정 게시글 조회")
    void getPostByPostId(){
        Post post = postReadService.getPostById(1L).get();
        assertThat(post.getUser().getUserId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("카테고리에 따른 게시글 전체 조회")
    void getPostAllByCategoryId(){
        List<Post> postList = postReadService.getPostAllByCategoryId(pageable,1).getContent();
        assertThat(postList.size()).isEqualTo(4);
    }

    @Test
    @DisplayName("인기글 전체 조회")
    void getPopularPostAll(){
        List<Post> postList = postReadService.getPopularPostAll(pageable).getContent();
        Post post = postRepository.findPostByPostId(1L).get();
        assertThat(post.getFavoriteCount()).isEqualTo(10);
    }
}
