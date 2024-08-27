package com.sparta.business.domain.master_customer.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserReviewRequestDto {
    private UUID storeId;
    private Integer rating;
    private String comment;
}
