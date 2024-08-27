package com.sparta.business.domain.owner.dto;

import lombok.*;

import java.util.UUID;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StoreEditRequestDto {

    private String name;
    private String address;
    private String phoneNumber;
    //    private UUID regionId;   // Region의 ID
    private UUID categoryId; // Category의 ID

}
