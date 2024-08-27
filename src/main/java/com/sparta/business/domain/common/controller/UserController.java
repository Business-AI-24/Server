package com.sparta.business.domain.common.controller;

import com.sparta.business.domain.master.service.CategoryService;
import com.sparta.business.domain.common.dto.*;
import com.sparta.business.domain.common.service.UserService;
import com.sparta.business.entity.Category;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/common")
public class UserController {

    private final UserService userService;
    private final CategoryService categoryService;

    @PostMapping("/sign_up")
    public ResponseEntity<String> signup(@Valid @RequestBody SignUpRequestDto requestDto) {
        userService.signup(requestDto);
        return ResponseEntity.ok().body("회원 가입이 완료되었습니다.");
    }

    @GetMapping("/orders/{order_id}")
    public ResponseEntity<OrderResponseDto> getOrder(@PathVariable UUID order_id) {
        return new ResponseEntity<>(userService.getOrder(order_id), HttpStatus.OK);
    }

    @GetMapping("/stores")
    public ResponseEntity<Page<StoreResponseDto>> getStores(@RequestParam("page") int page,
                                                      @RequestParam("limit") int limit,
                                                      @RequestParam("sort") Boolean sort){
        return ResponseEntity.ok(userService.getStores(page-1,limit,sort));
    }

    @GetMapping("/stores/{category_id}")
    public ResponseEntity<Page<StoreResponseDto>> getStoresForCategory(@RequestParam("page") int page,
                                                            @RequestParam("limit") int limit,
                                                            @RequestParam("sort") Boolean sort,
                                                                       @PathVariable UUID category_id){
        return ResponseEntity.ok(userService.getStoresForCategory(page-1,limit,sort,category_id));
    }

    @GetMapping("/products/{store_id}")
    public ResponseEntity<Page<ProductResponseDto>> getProductsForStore(@RequestParam("page") int page,
                                                                        @RequestParam("limit") int limit,
                                                                        @RequestParam("sort") Boolean sort,
                                                                        @PathVariable UUID store_id){
        return ResponseEntity.ok(userService.getProductsForStore(page-1,limit,sort,store_id));
    }

    @GetMapping("/notices")
    public ResponseEntity<Page<NoticeResponseDto>> getNotices(@RequestParam("page") int page,
                                                             @RequestParam("limit") int limit,
                                                             @RequestParam("sort") Boolean sort){
        return ResponseEntity.ok(userService.getNotices(page-1,limit,sort));
    }

    @GetMapping("/reviews/{store_id}")
    public ResponseEntity<Page<ReviewResponseDto>> getReviewsForStore(@RequestParam("page") int page,
                                                                        @RequestParam("limit") int limit,
                                                                        @RequestParam("sort") Boolean sort,
                                                                        @PathVariable UUID store_id){
        return ResponseEntity.ok(userService.getReviewsForStore(page-1,limit,sort,store_id));
    }


    //카테고리 조회
    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getCategories(){
        List<Category> categories = categoryService.getCategories();
        return ResponseEntity.ok(categories);
    }
}