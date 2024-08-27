package com.sparta.business.domain.master.dto;

import java.util.UUID;
import lombok.Getter;

@Getter
public class CommentRequestDto {
    private UUID complaint_id;
    private String title;
    private String content;
    private Boolean is_public;
}
