package com.sparta.business.domain.master.dto;

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
