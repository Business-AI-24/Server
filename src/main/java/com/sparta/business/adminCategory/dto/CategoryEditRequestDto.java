package com.sparta.business.adminCategory.dto;

import lombok.*;


import java.util.UUID;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryEditRequestDto {
    private UUID categoryId;
    String categoryType;
}
