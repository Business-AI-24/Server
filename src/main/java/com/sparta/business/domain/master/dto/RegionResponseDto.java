package com.sparta.business.domain.master.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class RegionResponseDto {
    private UUID region_id;
    private String region_name;

}
