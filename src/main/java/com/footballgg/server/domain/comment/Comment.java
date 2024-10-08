package com.footballgg.server.domain.comment;

import com.footballgg.server.base.basetime.BaseTimeEntity;
import com.footballgg.server.domain.post.Post;
import com.footballgg.server.domain.user.User;
import com.footballgg.server.dto.comment.CommentResponse;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Comment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    private String content;
}
