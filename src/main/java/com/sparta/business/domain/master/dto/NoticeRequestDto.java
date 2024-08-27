package com.sparta.business.domain.master.dto;

import lombok.Getter;

@Getter
public class NoticeRequestDto {
    private String title;
    private String content;
    private Boolean is_public;
}
