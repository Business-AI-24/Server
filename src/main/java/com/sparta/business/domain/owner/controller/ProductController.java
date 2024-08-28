package com.sparta.business.domain.owner.controller;

import com.sparta.business.domain.owner.dto.ProductEditRequestDto;
import com.sparta.business.domain.owner.dto.ProductRequestDto;
import com.sparta.business.domain.owner.service.ProductService;
import com.sparta.business.entity.User;
import com.sparta.business.filter.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/owner")
public class ProductController {

    private final ProductService productService;


    //메뉴 등록
    @PostMapping("/product")
    public ResponseEntity<String> addProduct(
            @RequestBody ProductRequestDto productRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        //인증된 사용자 정보 가져오기
        User user = userDetails.getUser();
        log.info("productRequestDto: {}", productRequestDto);
        log.info("user: {}", user);


        return productService.addProduct(productRequestDto,user);
    }


    //메뉴 수정
    @PutMapping("/product/{product_id}")
    public ResponseEntity<String> updateProduct(
            @PathVariable UUID product_id,
            @RequestBody ProductEditRequestDto productEditRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        System.out.println("productEditRequestDto: " + productEditRequestDto);
        System.out.println("userDetails: " + userDetails);

        return productService.updateProduct(product_id,productEditRequestDto,userDetails.getUser());
    }

    //메뉴 삭제
    @DeleteMapping("/product/{product_id}/hide")
    public ResponseEntity<String> hideProduct(
            @PathVariable UUID product_id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        //로그인한 사용자의 ID
        String username = userDetails.getUsername();


        System.out.println("username: " + username);
        System.out.println("product_id: " + product_id);

        return productService.deleteProduct(username,product_id);
    }









}
