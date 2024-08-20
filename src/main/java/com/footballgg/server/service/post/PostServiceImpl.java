package com.footballgg.server.service.post;

import com.footballgg.server.domain.post.Post;
import com.footballgg.server.dto.post.SavePostRequest;
import com.footballgg.server.dto.post.UpdatePostRequest;
import com.footballgg.server.repository.post.PostRepository;
import com.footballgg.server.domain.user.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostServiceImpl implements PostService{
    private final PostRepository postRepository;

    @Override
    @Transactional
    public Post savePost(SavePostRequest savePostRequest, User user) {
        Post post = Post.builder()
                .title(savePostRequest.getTitle())
                .content(savePostRequest.getContent())
                .categoryId(savePostRequest.getCategoryId())
                .user(user)
                .view(1L)
                .build();
        return postRepository.save(post);
    }

    @Override
    @Transactional
    public void deletePost(Long postId, User user) {
        Post post = postRepository.findPostByPostId(postId)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.BAD_REQUEST,"존재하지 않는 게시글입니다."));
        if(user == null || user.getUserId()!=post.getUser().getUserId()){
            log.info("삭제 권한이 없음");
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"삭제 권한이 없습니다.");
        }
        postRepository.deletePostByPostId(post.getPostId());
    }

    @Override
    @Transactional
    public Post updatePost(Long postId,UpdatePostRequest updatePostRequest, User user) {
        Post post = postRepository.findPostByPostId(postId)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.BAD_REQUEST,"존재하지 않는 게시글입니다."));
        if(user == null || user.getUserId()!=post.getUser().getUserId()){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"수정 권한이 없습니다.");
        }
        Post updatedPost = post.toBuilder()
                .title(updatePostRequest.getTitle())
                .content(updatePostRequest.getContent())
                .build();
        Post result = postRepository.save(updatedPost);
        return result;
    }

    /** 이미지 url 추출 */
    @Transactional
    public List<String> extractImageUrl(String content) {
        Document doc = Jsoup.parse(content);
        Elements elements =  doc.getElementsByTag("img");

        List<String> imgurl = new ArrayList<>();
        for (Element element : elements) {
            imgurl.add(element.attr("src")); // src에서 url 추출
        }

        return imgurl;
    }
}
