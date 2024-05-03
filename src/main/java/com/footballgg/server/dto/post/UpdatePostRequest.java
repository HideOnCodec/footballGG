package com.footballgg.server.dto.post;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
@Builder
public class UpdatePostRequest {
    private String title;
    private String content;
    private int categoryId;
}
