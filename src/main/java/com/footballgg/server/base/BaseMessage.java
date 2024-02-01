package com.footballgg.server.base;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class BaseMessage {
    private String message;
    private String redirectUrl;
    private Map<String, Object> data;
}
