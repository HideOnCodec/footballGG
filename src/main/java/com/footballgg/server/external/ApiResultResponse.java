package com.footballgg.server.external;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApiResultResponse {
    private String dateFrom;
    private String dateTo;
    private ApiResponse apiResponse;
}
