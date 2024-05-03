package com.footballgg.server.controller;

import com.footballgg.server.dto.TestDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class TestController {
    private final List<Integer> list = new ArrayList<>();

    @GetMapping("img-test")
    public String imgTestForm(Model model){
        TestDto testDto = new TestDto();
        testDto.setNumList(new ArrayList<>());
        model.addAttribute("testDto",testDto);
        return "test";
    }
    @PostMapping("img-test")
    public String imgTest(@ModelAttribute TestDto testDto, Model model){
        list.add(testDto.getNum());
        model.addAttribute("numList",list);
        return "test";
    }
}
