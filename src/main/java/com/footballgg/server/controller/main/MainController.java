package com.footballgg.server.controller.main;

import com.footballgg.server.domain.post.Post;
import com.footballgg.server.domain.user.User;
import com.footballgg.server.external.ApiResponse;
import com.footballgg.server.external.ApiResultResponse;
import com.footballgg.server.external.LeagueScheduleService;
import com.footballgg.server.service.post.PostReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final PostReadService postReadService;
    private final LeagueScheduleService leagueScheduleService;

    /** 메인 화면 */
    @GetMapping("/")
    public String index(@RequestParam(defaultValue = "0") int week, @RequestParam(defaultValue = "PL") String leagueCode, @PageableDefault(size = 5) Pageable pageable, Model model, @AuthenticationPrincipal User user) {
        Page<Post> popularList = postReadService.getPopularPostAll(pageable);
        ApiResultResponse apiResultResponse = leagueScheduleService.getMatchesForWeek(leagueCode,week);
        ApiResponse apiResponse = apiResultResponse.getApiResponse();

        model.addAttribute("season",apiResponse.getFilters().getSeason());
        model.addAttribute("dateTo",apiResultResponse.getDateTo());
        model.addAttribute("dateFrom",apiResultResponse.getDateFrom());
        model.addAttribute("week",week);
        model.addAttribute("apiResponse",apiResponse);
        model.addAttribute("popularList",popularList);
        model.addAttribute("endPage",popularList.getTotalPages()-1);
        model.addAttribute("start",popularList.getNumber()-5);
        model.addAttribute("end",popularList.getNumber()+5);

        return "index";
    }
}
