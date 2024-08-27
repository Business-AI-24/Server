package com.sparta.business.domain.master_customer.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserComplaintsRequestDto {
    private UUID storeId;
    private String title;
    private String content;
}
