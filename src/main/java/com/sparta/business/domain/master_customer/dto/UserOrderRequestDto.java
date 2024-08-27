package com.sparta.business.domain.master_customer.dto;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserOrderRequestDto {
    private String request;
    private Boolean is_public;
    private Map<UUID, Long> productIdCountMap;

    private String pgName;
    private String transactionID;
}
