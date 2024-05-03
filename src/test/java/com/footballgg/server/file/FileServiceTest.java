package com.footballgg.server.file;

import com.footballgg.server.domain.file.FileMapping;
import com.footballgg.server.domain.post.Post;
import com.footballgg.server.domain.user.Role;
import com.footballgg.server.domain.user.User;
import com.footballgg.server.domain.user.UserType;
import com.footballgg.server.repository.file.FileMappingRepository;
import com.footballgg.server.repository.post.PostRepository;
import com.footballgg.server.repository.user.UserRepository;
import com.footballgg.server.service.file.FileServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.yml")
public class FileServiceTest {
    @Autowired
    private FileServiceImpl fileService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;

    private final String imgUrl = "testUrl";
    @Test
    void createFileMapping(){
        FileMapping result = fileService.createFileMapping(imgUrl);
        assertThat(result.getFileUrl()).isEqualTo(imgUrl);
        assertThat(result.getPostId()).isEqualTo(null);
    }

    @Test
    void updateFileMapping(){
        User user = User.builder()
                .userType(UserType.EMAIL)
                .email("test@naver.com")
                .nickname("test")
                .password("test1234")
                .role(Role.ROLE_USER)
                .build();
        userRepository.save(user);
        Post post = Post.builder()
                .title("test")
                .content("test")
                .categoryId(1)
                .user(user)
                .build();
        postRepository.save(post);

        List<FileMapping> fileMappingList = fileService.updateFileMapping(post,imgUrl);
        FileMapping result = fileMappingList.get(0);
        assertThat(result.getPostId()).isEqualTo(1);
    }
}
