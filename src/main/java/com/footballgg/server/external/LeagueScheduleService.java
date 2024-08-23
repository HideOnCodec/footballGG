package com.footballgg.server.external;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor
public class LeagueScheduleService {

    private final String API_URL = "http://api.football-data.org/";

    @Value("${football.api.key}")
    private String apiKey;

    public ApiResultResponse getMatchesForWeek(String leagueCode, int week) {
        LocalDate dateFrom;
        if(week==0) dateFrom = DateUtils.getStartOfWeek(LocalDate.now());
        else if(week>0) dateFrom = DateUtils.getStartOfWeek(LocalDate.now()).plusWeeks(week);
        else dateFrom = DateUtils.getStartOfWeek(LocalDate.now()).minusWeeks(week);

        String dateTo = DateUtils.getEndOfWeek(dateFrom).toString();
        WebClient webClient = WebClient.builder().baseUrl(API_URL).build();

        ApiResponse apiResponse = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v4/competitions/"+leagueCode+"/matches")
                        .queryParam("dateFrom", dateFrom.toString())
                        .queryParam("dateTo", dateTo)
                        .build())
                .header("X-Auth-Token", apiKey)
                .accept(MediaType.APPLICATION_JSON)// API Key를 헤더에 포함
                .retrieve()
                .bodyToMono(ApiResponse.class)
                .block();
        return ApiResultResponse.builder()
                .apiResponse(convertUtcToKst(apiResponse))
                .dateTo(dateTo)
                .dateFrom(dateFrom.toString())
                .build();
    }

    private ApiResponse convertUtcToKst(ApiResponse apiResponse) {
        // 한국 시간대로 변환하여 매치 정보에 적용
        apiResponse.getMatches().forEach(match -> {
            ZonedDateTime utcDateTime = ZonedDateTime.parse(match.getUtcDate());  // UTC 시간 가져오기
            ZonedDateTime kstDateTime = utcDateTime.withZoneSameInstant(ZoneId.of("Asia/Seoul"));  // 한국 시간대로 변환
            match.setUtcDate(String.valueOf(kstDateTime));  // 변환된 시간 설정
        });
        return apiResponse;
    }
}
