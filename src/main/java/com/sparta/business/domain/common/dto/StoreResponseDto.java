package com.sparta.business.domain.common.dto;

import com.sparta.business.entity.Store;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StoreResponseDto {

    private String name;
    private String category;
    private String address;
    private String phoneNumber;
    private Boolean is_open;
}
