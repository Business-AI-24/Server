package com.sparta.business.domain.open_api;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AiRequestDto {

    private List<Content> contents;

    @Getter
    @Setter
    public static class Content {
        private List<Part> parts;
    }

    @Getter
    @Setter
    public static class Part {
        private String text;
    }
}
