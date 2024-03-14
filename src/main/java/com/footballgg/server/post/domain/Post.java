package com.footballgg.server.post.domain;


import com.footballgg.server.base.basetime.BaseTimeEntity;
import com.footballgg.server.user.domain.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Builder(toBuilder = true)
public class Post extends BaseTimeEntity {
    @Id
    @Column(name = "post_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @ColumnDefault("0")
    private Long view;

    @ManyToOne // Post(N) : User(1)
    @JoinColumn(name="user_id")
    private User user;

    @Column(name = "category_id")
    private int categoryId;

    @ColumnDefault("0")
    private int favoriteCount;
}
