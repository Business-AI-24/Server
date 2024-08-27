package com.sparta.business.domain.master.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NoticeResponseDto {
    private UUID noticeId;
    private String message;
}
