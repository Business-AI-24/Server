package com.sparta.business.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StoreCategoryEnum {

    한식("한식"),
    중식("중식"),
    분식("분식"),
    치킨("치킨"),
    피자("피자");

    private String category;
}
