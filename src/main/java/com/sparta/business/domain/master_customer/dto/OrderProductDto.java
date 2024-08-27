package com.sparta.business.domain.master_customer.dto;

import com.sparta.business.entity.OrderProduct;
import lombok.Getter;

@Getter
public class OrderProductDto {
    private Long count;
    private String productName;

    public OrderProductDto(OrderProduct orderProduct){
        this.count = orderProduct.getCount();
        this.productName = orderProduct.getProduct().getName();
    }
}
