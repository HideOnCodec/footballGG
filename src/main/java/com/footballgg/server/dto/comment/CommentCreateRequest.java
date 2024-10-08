package com.footballgg.server.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CommentCreateRequest {
    @NotNull
    private Long postId;
    @NotBlank
    private String content;
}
