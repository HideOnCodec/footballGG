package com.footballgg.server.dto.post;

import com.footballgg.server.domain.post.Category;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
@Builder
public class UpdatePostRequest {
    @NotEmpty(message = "제목을 입력해주세요")
    private String title;
    @NotEmpty(message = "내용을 입력해주세요")
    private String content;
    @NotNull(message = "카테고리를 선택해주세요")
    private Category category;
}
