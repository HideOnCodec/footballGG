package com.footballgg.server.post.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data // getter/setter, requiredArgsController, ToString 등 합쳐놓은 세트
@Getter
@Builder
public class UpdatePostRequest {
    private Long postId;
    private String title;
    private String content;
    private int categoryId;
}
