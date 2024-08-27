package com.sparta.business.domain.master_customer.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserOrderResponseDto {
    private UUID orderId;
    private String message;
}
