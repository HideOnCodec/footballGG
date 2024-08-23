package com.footballgg.server.post;


import com.footballgg.server.domain.post.Category;
import com.footballgg.server.domain.post.Post;
import com.footballgg.server.dto.post.PostResponse;
import com.footballgg.server.dto.post.SavePostRequest;
import com.footballgg.server.dto.post.UpdatePostRequest;
import com.footballgg.server.service.post.PostServiceImpl;
import com.footballgg.server.domain.user.User;
import com.footballgg.server.repository.user.UserRepository;
import com.footballgg.server.domain.user.Role;
import com.footballgg.server.domain.user.UserType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // 클래스 단위로 생명주기를 바꾸면 static을 붙일 필요가 없어짐
@TestPropertySource(locations = "classpath:application-test.yml")
public class PostServiceTest {
    private User user;
    @Autowired
    private PostServiceImpl postService;
    @Autowired
    private UserRepository userRepository;

    @BeforeAll
    void setting(){
        this.user = User.builder()
                .userId(1L)
                .userType(UserType.EMAIL)
                .email("tlsdmsgp33@naver.com")
                .nickname("엘모")
                .password("1234")
                .role(Role.ROLE_USER)
                .build();
        userRepository.save(this.user);
    }
    @Test
    @DisplayName("게시글 저장 테스트")
    void savePost(){
        SavePostRequest postRequest = SavePostRequest.builder()
                .category(Category.PREMIER.toString())
                .content("test")
                .title("test")
                .build();

        PostResponse post = postService.savePost(postRequest,user);
        assertThat(post.getTitle()).isEqualTo("test");
        assertThat(post.getPostId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("게시글 수정 테스트")
    void updatePost(){
        UpdatePostRequest updatePostRequest = UpdatePostRequest.builder()
                .title("update test")
                .content("update test")
                .build();
        PostResponse post = postService.updatePost(1L,updatePostRequest,user);
        assertThat(post.getTitle()).isEqualTo("update test");
        assertThat(post.getCategory()).isEqualTo(Category.PREMIER);
        assertThat(post.getUserId()).isEqualTo(user.getUserId());
    }

    @Test
    @DisplayName("게시글 삭제 테스트")
    void deletePost(){
        postService.deletePost(1L,this.user);
    }
}
