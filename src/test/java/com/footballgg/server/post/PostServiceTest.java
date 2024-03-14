package com.footballgg.server.post;


import com.footballgg.server.post.domain.Post;
import com.footballgg.server.post.dto.SavePostRequest;
import com.footballgg.server.post.dto.UpdatePostRequest;
import com.footballgg.server.post.repository.PostRepository;
import com.footballgg.server.post.service.PostService;
import com.footballgg.server.post.service.PostServiceImpl;
import com.footballgg.server.user.domain.User;
import com.footballgg.server.user.repository.UserRepository;
import com.footballgg.server.user.usertype.Role;
import com.footballgg.server.user.usertype.UserType;
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
                .categoryId(1)
                .content("test")
                .title("test")
                .build();

        Post post = postService.savePost(postRequest,user);
        assertThat(post.getTitle()).isEqualTo("test");
        assertThat(post.getPostId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("게시글 수정 테스트")
    void updatePost(){
        UpdatePostRequest updatePostRequest = UpdatePostRequest.builder()
                .postId(1L)
                .title("update test")
                .content("update test")
                .build();
        Post post = postService.updatePost(updatePostRequest,user);
        assertThat(post.getTitle()).isEqualTo("update test");
        assertThat(post.getCategoryId()).isEqualTo(1);
        assertThat(post.getUser().getUserId()).isEqualTo(user.getUserId());
    }

    @Test
    @DisplayName("게시글 삭제 테스트")
    void deletePost(){
        Boolean result = postService.deletePost(1L);
        assertThat(result).isEqualTo(true);
        Boolean result2 = postService.deletePost(1L);
        assertThat(result2).isEqualTo(false);
    }
}
