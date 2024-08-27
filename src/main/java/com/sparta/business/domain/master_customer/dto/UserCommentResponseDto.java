package com.sparta.business.domain.master_customer.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserCommentResponseDto {
    private UUID commentId;
    private String title;
    private String content;
}
