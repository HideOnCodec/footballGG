package com.footballgg.server.domain.file;

import com.footballgg.server.domain.post.Post;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(toBuilder = true)
@Getter
public class FileMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Long fileId;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post postId;

    @Column(name = "file_url")
    @NotEmpty
    private String fileUrl;
}
