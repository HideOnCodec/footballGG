package com.footballgg.server.domain.post;


import com.footballgg.server.base.basetime.BaseTimeEntity;
import com.footballgg.server.domain.favorite.Favorite;
import com.footballgg.server.domain.file.FileMapping;
import com.footballgg.server.domain.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

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

    @ManyToOne(fetch = FetchType.LAZY) // Post(N) : User(1)
    @JoinColumn(name="user_id")
    private User user;

    @Column(nullable = false)
    @Convert(converter = CategoryConverter.class)
    private Category category;

    @Builder.Default
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Favorite> favoriteList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<FileMapping> fileMappingList = new ArrayList<>();

    @ElementCollection
    private List<String> imgUrl;
}
