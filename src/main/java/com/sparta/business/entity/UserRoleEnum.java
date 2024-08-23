package com.sparta.business.entity;

import jakarta.persistence.GeneratedValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
public enum UserRoleEnum {

    CUSTOMER("CUSTOMER"),
    OWNER("OWNER"),
    MASTER("MASTER");

    private String authority;
}
