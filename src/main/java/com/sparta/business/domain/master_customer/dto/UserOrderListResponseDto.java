package com.sparta.business.domain.master_customer.dto;

import com.sparta.business.entity.Order;
import com.sparta.business.entity.OrderProduct;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserOrderListResponseDto {
    private UUID orderId;
    private Long totalPrice;
    private String request;
    private String storeName;
    private List<OrderProductDto> orderProducts;

    public UserOrderListResponseDto(Order order) {
        this.orderId = order.getId();
        this.totalPrice = order.getTotalPrice();
        this.request = order.getRequest();
        this.storeName = order.getStore().getName();
        this.orderProducts = order.getOrderProductList().stream()
            .map(OrderProductDto::new)
            .collect(Collectors.toList());
    }
}