package com.footballgg.server.domain.post;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Category {
    PREMIER("프리미어리그"),
    LALIGA("라리가"),
    SERIEA("세리에A"),
    BUNDESLIGA("분데스리가");

    private final String name;
}
