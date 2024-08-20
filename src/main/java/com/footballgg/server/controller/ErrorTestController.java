package com.footballgg.server.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class ErrorTestController {
    @GetMapping("/500")
    public void error_500(){
        throw new RuntimeException();
    }

    @GetMapping("/404")
    public void error_404(){
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/403")
    public void error_4xx(){
        throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/502")
    public void error_5xx(){
        throw new ResponseStatusException(HttpStatus.BAD_GATEWAY);
    }


}
