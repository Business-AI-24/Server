package com.sparta.business.domain.owner.dto;


import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductEditRequestDto {
    private String productName;
    private String productDescription;
    private Long productPrice;
    private String productCategory;
    private UUID storeId;
}
