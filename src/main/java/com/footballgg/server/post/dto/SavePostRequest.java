package com.footballgg.server.post.dto;

import com.footballgg.server.post.domain.Post;
import com.footballgg.server.user.domain.User;
import com.footballgg.server.user.usertype.Role;
import com.footballgg.server.user.usertype.UserType;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data // getter/setter, requiredArgsController, ToString 등 합쳐놓은 세트
@Getter
@Builder
public class SavePostRequest {
    @NotEmpty(message = "제목을 입력해주세요")
    private String title;
    @NotEmpty(message = "내용을 입력해주세요")
    private String content;
    @NotEmpty(message = "카테고리를 선택해주세요")
    private int categoryId;
}
