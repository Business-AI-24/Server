package com.sparta.business.domain.master.controller;

import com.sparta.business.domain.master.dto.CategoryEditRequestDto;
import com.sparta.business.domain.master.dto.CategoryRequestDto;
import com.sparta.business.domain.master.service.CategoryService;
import com.sparta.business.filter.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class CategoryController {

    private final CategoryService categoryService;


    //카테고리 등록
    @PostMapping("/categories")
    public ResponseEntity<String> addCategory(
            @RequestBody CategoryRequestDto categoryRequestDto ,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String categoryType = categoryRequestDto.getCategoryType();
        System.out.println("categoryTypes: " + categoryType);
        System.out.println("userDetails: " + userDetails);
        return categoryService.addCategory(categoryType,userDetails.getUser());

    }

    //카테고리 수정
    @PutMapping("/categories/{category_id}")
    public ResponseEntity<String> updateCategory(
            @PathVariable UUID category_id,
            @RequestBody CategoryEditRequestDto categoryEditRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails)
           {
               //로그인한 사용자의 ID를 얻는 방법
             String username = userDetails.getUsername();
//             String categoryType = categoryEditRequestDto.getCategoryType();
        System.out.println("categoryEditRequestDto: " + categoryEditRequestDto);
        System.out.println("user: " + username);
        System.out.println("category_id: " + category_id);
               return categoryService.updateCategory(category_id, categoryEditRequestDto, username);


    }

    //카테고리 삭제
    @DeleteMapping("/categories/{category_id}")
    public ResponseEntity<String> deleteCategory(
            @PathVariable UUID category_id,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ){

        //로그인한 사용자의 ID 얻는 방법
        String username = userDetails.getUsername();

        System.out.println("categoryId: " + category_id);
        System.out.println("user: " + username);

        return categoryService.deleteCategory(category_id,username);

    }



    }




