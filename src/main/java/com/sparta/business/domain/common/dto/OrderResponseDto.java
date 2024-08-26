package com.sparta.business.domain.common.dto;

import com.sparta.business.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class OrderResponseDto {
    private UUID order_id;
    private Long user_id;
    private Long total_price;
    private String address;
    private String request;

}
