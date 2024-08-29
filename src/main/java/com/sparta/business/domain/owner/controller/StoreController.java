package com.sparta.business.domain.owner.controller;


import com.sparta.business.domain.owner.dto.StoreEditRequestDto;
import com.sparta.business.domain.owner.dto.StoreRequestDto;
import com.sparta.business.domain.owner.service.StoreService;
import com.sparta.business.filter.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/owner")
public class StoreController {

    @Autowired
    private final StoreService storeService;

    // 상점 등록
    @PostMapping("/store")
    public ResponseEntity<String> createStore(
            @RequestBody StoreRequestDto storeRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        try {
            ResponseEntity<String> store = storeService.createStore(storeRequestDto, userDetails.getUser());
            return ResponseEntity.ok("상점이 성공적으로 등록되었습니다.");
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("상품 등록이 실패했습니다.");
        }
    }

    //상점 수정
    @PutMapping("/store/{store_id}")
    public ResponseEntity<String> updateStore(
            @PathVariable UUID store_id,
            @RequestBody StoreEditRequestDto storeEditRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails){

        //로그인한 사용자의 ID를 얻는 방법
        String username = userDetails.getUsername();


        return storeService.updateStore(store_id,storeEditRequestDto,username);
    }

    //상점 삭제
    @DeleteMapping("/store/{store_id}")
    public ResponseEntity<String> deleteStore(
            @PathVariable UUID store_id,
            @AuthenticationPrincipal UserDetailsImpl userDetails){
        //로그인한 사용자의 ID
        String username = userDetails.getUsername();


        return storeService.deleteStore(username,store_id);

    }


    //음식점 주문 거절
    @DeleteMapping("/order/{order_id}/reject")

    public ResponseEntity<String> rejectOrder(
            @PathVariable UUID order_id,
            @AuthenticationPrincipal UserDetailsImpl userDetails){
        //로그인한 사용자의 ID

        String username = userDetails.getUsername();


        return storeService.rejectOrder(username,order_id);
    }




}
